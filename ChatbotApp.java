import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import okhttp3.*;


class Question {
    String text;

    public Question(String text) {
        this.text = text;
    }
}

class Response {
    String text;

    public Response(String text) {
        this.text = text;
    }
}

public class ChatbotApp {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/engines/davinci-codex/completions";
    private static final String API_KEY = "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";
    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final Request.Builder requestBuilder = ((Object) new Request.Builder())
            .addHeader("Authorization", "Bearer " + API_KEY)
            .addHeader("Content-Type", "application/json");

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Chatbot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // Create the text input field
        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(400, 50));

        // Create the text area to display the responses
        JTextArea responseArea = new JTextArea();
        responseArea.setEditable(false);

        // Create the button to send the question to the chatbot
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = inputField.getText();

                // Send the question to the chatbot and receive the response
                try {
                    Response response = sendQuestion(new Question(question));

                    // Display the response in the text area
                    responseArea.append(response.text + "\n");
                } catch (IOException ex) {
                    responseArea.append("Error: " + ex.getMessage() + "\n");
                }

                // Clear the input field
                inputField.setText("");
            }
        });

        // Create the panel to hold the input field and send button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.add(inputField);
        inputPanel.add(sendButton);

        // Add the components to the main frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(responseArea), BorderLayout.CENTER);

        // Show the main frame
        frame.setVisible(true);
    }

    private static Response sendQuestion(Question question) throws IOException {
        String json = String.format("{\"prompt\": \"%s\"}", question.text);
        RequestBody requestBody = RequestBody.create(json, JSON);

        Request request = ((Object) requestBuilder.url(API_ENDPOINT)).post(requestBody).build();

        ResponseBody responseBody = ((Object) httpClient.newCall(request)).execute().body();

        String responseJson = responseBody.toString();
        String responseText = responseJson.split("\"text\": \"")[1].split("\"")[0];
        Response response = new Response(responseText);

        return response;
    }
}