package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.*;

@SpringBootApplication
@Theme(value = "merodemicroservicesbuilderforiot")
public class Application implements AppShellConfigurator {

    public static void main(String[] args) throws IOException, InterruptedException {

        SpringApplication.run(Application.class, args);

        //Start the Python Simulated Station
        //simulatedStation();

    }

    @PostConstruct
    public void init() {
        // Specifica il percorso della directory del progetto
        String projectPath = "IoT-EDG-Rest-Services";

        try {
            // Esegui 'mvn package'
            if (runCommand(new String[]{"cmd.exe", "/c", "mvn package"}, projectPath)) {
                // Se 'mvn package' ha successo, esegui gli script bash in sequenza
                if (runCommand(new String[]{"cmd.exe", "/c", "extras\\start-db-server.bat"}, projectPath)) {
                    if (runCommand(new String[]{"cmd.exe", "/c", "extras\\init-db.bat"}, projectPath)) {
                        runCommand(new String[]{"cmd.exe", "/c", "extras\\start-service.bat"}, projectPath);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean runCommand(String[] commands, String directory) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.directory(new java.io.File(directory));
        processBuilder.redirectErrorStream(true);

        System.out.println("Esecuzione comando: " + String.join(" ", commands));
        Process process = processBuilder.start();

        // Leggi l'output dal processo
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        // Controlla il codice di uscita del processo
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.out.println("Il comando non è riuscito ad eseguirsi correttamente, codice di errore: " + exitCode);
            return false;
        } else {
            System.out.println("Comando eseguito con successo.");
            return true;
        }
    }

    private static void simulatedStation() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("py", "Simulated Python Station/main.py");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.out.println("Errore durante la generazione della Simulated IoT Sttion.");
            } else {
                System.out.println("Simulated IoT Station generata con successo!");

                String url = "http://localhost:8081";
                String chromePath = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe"; // Inserisci il percorso corretto di Chrome

                ProcessBuilder browserProcessBuilder = new ProcessBuilder(chromePath, url);

                try {
                    browserProcessBuilder.start();
                } catch (IOException e) {
                    System.err.println("Errore durante l'apertura di Google Chrome: " + e.getMessage());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
