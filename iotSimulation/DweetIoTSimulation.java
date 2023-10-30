package no.hvl.dat250.feedapp.iotSimulation;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class DweetIoTSimulation {
    private int greenVotes = 0;
    private int redVotes = 0;
    private String question;

     public DweetIoTSimulation() {
    }

    public DweetIoTSimulation(String question) {
        this.question = question;
    }

    public void sendDeviceData() {
        try {

            // Simulate data to send to Dweet.io as JSON
            String deviceData = "{\"question\": \"" + question + "\", \"greenVotes\": " + greenVotes + ", \"redVotes\": " + redVotes + "}";

            // Create a URL for the Dweet.io service
            URL url = new URL("https://dweet.io/dweet/for/<your-device-name>");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP request method to POST
            connection.setRequestMethod("POST");

            // Enable input/output streams for sending data
            connection.setDoOutput(true);

            // Set the content type to JSON
            connection.setRequestProperty("Content-Type", "application/json");

            // Write the device data to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = deviceData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the HTTP response code
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                System.out.println("Data sent successfully.");
            } else {
                System.out.println("Failed to send data. Response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementGreenVotes() {
        greenVotes++;
        System.out.println("Yes vote added. Total yes votes: " + greenVotes);
    }

    public void incrementRedVotes() {
        redVotes++;
        System.out.println("No vote added. Total no votes: " + redVotes);
    }

    public void resetVotes() {
        greenVotes = 0;
        redVotes = 0;
        System.out.println("Votes reset. Total yes votes: 0, Total no votes: 0");
    }

    // public static String fetchQuestionFromDatabase() {
    //     String question = null;
    //     try {
    //         // Replace these values with your database connection details
    //         String jdbcUrl = "jdbc:mysql://localhost:3306/your_database";
    //         String username = "your_username";
    //         String password = "your_password";

    //         Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

    //         // Replace this query with your SQL query to retrieve the question
    //         String query = "SELECT question FROM questions WHERE question_id = ?";
    //         int questionId = 1; // Replace with the actual question ID
    //         PreparedStatement preparedStatement = connection.prepareStatement(query);
    //         preparedStatement.setInt(1, questionId);

    //         ResultSet resultSet = preparedStatement.executeQuery();

    //         if (resultSet.next()) {
    //             question = resultSet.getString("question");
    //         }

    //         resultSet.close();
    //         preparedStatement.close();
    //         connection.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return question;
    // }

    public static void main(String[] args) {

        //String question = fetchQuestionFromDatabase();
        DweetIoTSimulation device = new DweetIoTSimulation();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Choose an action:");
                System.out.println("1. Press 'G' for a yes vote");
                System.out.println("2. Press 'R' for a no vote");
                System.out.println("3. Press 'S' to send votes to Dweet.io");
                System.out.println("4. Press 'X' to reset votes");
                System.out.println("5. Press 'Q' to quit");

                String input = scanner.nextLine().trim().toUpperCase();

                switch (input) {
                    case "G":
                        device.incrementGreenVotes();
                        break;
                    case "R":
                        device.incrementRedVotes();
                        break;
                    case "S":
                        device.sendDeviceData();
                        break;
                    case "X":
                        device.resetVotes();
                        break;
                    case "Q":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input. Please choose a valid option.");
                }
            }
        }
    }
}