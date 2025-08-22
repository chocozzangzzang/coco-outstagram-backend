package coco.project.demo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() throws Exception {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-adminsdk.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK가 성공적으로 초기화되었습니다.");
            }
        } catch (IOException e) {
            System.err.println("Firebase Admin SDK 초기화 오류: " + e.getMessage());
            throw e; // 애플리케이션 시작을 중단
        }
    }
}
