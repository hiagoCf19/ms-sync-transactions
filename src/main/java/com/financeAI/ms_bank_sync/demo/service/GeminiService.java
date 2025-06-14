package com.financeAI.ms_bank_sync.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financeAI.ms_bank_sync.demo.enums.TransactionCategory;

import com.financeAI.ms_bank_sync.demo.mapper.TransactionCategoryName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class GeminiService {

    @Value("${google.api.key}")
    private String apiKey;

    public TransactionCategoryName iaGenerateCategoryAndName(String description) {
        String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey;

        try {
            String prompt = buildPrompt(description);
            String promptEscaped = prompt
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n");

            String requestBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "%s"
                }
              ]
            }
          ]
        }
        """.formatted(promptEscaped);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            if (response.statusCode() != 200) {
                throw new RuntimeException("Erro ao chamar a API da IA. Status: " + response.statusCode());
            }

            // Verificação de erros conhecidos no corpo da resposta
            if (body.contains("quota") || body.contains("exceeded")) {
                throw new RuntimeException("Créditos da API esgotados ou limite excedido.");
            } else if (body.contains("API key") && body.contains("invalid")) {
                throw new RuntimeException("Chave da API inválida.");
            }

            TransactionCategoryName categoryName = extrairCategoriaENome(body);
            System.out.println("Resposta da IA:\n" + categoryName);

            return categoryName;

        } catch (RuntimeException e) {
            System.err.println("Erro conhecido: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado:");
            e.printStackTrace();
            return null;
        }
    }

    private String buildPrompt(String transactionDescription){
        return """
            You are a smart financial assistant.
                
                            Your task is to classify a bank transaction based on its description and generate a short, clear, and user-friendly name suitable for display in financial statements.
                
                            ### Categories:
                            HOUSING, TRANSPORTATION, FOOD, ENTERTAINMENT, HEALTH, UTILITY, SALARY, EDUCATION, TRIP, INVOICES, OTHER
                
                            ### Instructions:
                            - Respond **only** in JSON format, structured like this:
                            {
                              "type": "CATEGORY",
                              "name": "Short and clear display name"
                            }
                            - The "type" must match one of the categories above.
                            - The "name" must be **concise and readable**, capturing the **purpose** or **counterparty** of the transaction.
                            - If the description includes a recipient or company name (e.g., in PIX transfers), include it in the "name".
                
                            ### Examples:
                
                            **Input:**
                            "Pagamento de boleto efetuado - DAS-SIMPLES NACIONAL" \s
                            **Output:**
                            {
                              "type": "INVOICES",
                              "name": "Pagamento DAS"
                            }
                
                            **Input:**
                            "Transferência enviada pelo Pix - FARMACIA SUPER POPULAR MASTER FARMA - 01.707.026/0001-37 - PAGSEGURO INTERNET IP S.A. (0290) Agência: 1 Conta: 55105661-7" \s
                            **Output:**
                            {
                              "type": "HEALTH",
                              "name": "Pix para FARMACIA SUPER POPULAR"
                            }
                
                            Now classify the following transaction:
                            "%s"
""".formatted(transactionDescription);
    }

    private TransactionCategoryName extrairCategoriaENome(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode candidates = root.path("candidates");
            if (!candidates.isArray() || candidates.isEmpty()) return null;

            JsonNode textNode = candidates.get(0).path("content").path("parts").get(0).path("text");
            if (textNode.isMissingNode() || textNode.isNull()) return null;

            String respostaIA = textNode.asText();

            // Limpa o texto removendo blocos markdown ```json ... ```
            // Regex que captura o conteúdo JSON dentro do bloco
            String jsonLimpo = respostaIA.replaceAll("(?s)```json\\s*(.*?)\\s*```", "$1").trim();

            // Parseia a resposta JSON limpa
            JsonNode result = mapper.readTree(jsonLimpo);
            String type = result.path("type").asText(null);
            String name = result.path("name").asText(null);

            if (type == null || name == null) return null;

            TransactionCategory category;
            try {
                category = TransactionCategory.valueOf(type.toUpperCase(Locale.ROOT).trim());
            } catch (IllegalArgumentException e) {
                category = TransactionCategory.OTHER;
            }

            return new TransactionCategoryName(category, name.trim());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private TransactionCategory transformIAResponseInTransactionType(String iaResponse) {
        if (iaResponse == null || iaResponse.isBlank()) {
            return TransactionCategory.OTHER;
        }

        // Limpa a string: remove espaços extras e coloca em caixa alta
        String clean = iaResponse.trim().toUpperCase(Locale.ROOT);

        try {
            return TransactionCategory.valueOf(clean);
        } catch (IllegalArgumentException e) {
            // Se não for um valor válido do enum, retorna OTHER
            System.err.println("Categoria inválida recebida da IA: " + iaResponse);
            return TransactionCategory.OTHER;
        }
    }
}
