package com.example.v2ourbook.services;
import com.example.v2ourbook.error.ExceptionBlueprint;
import com.example.v2ourbook.models.GetBookProcess;
import com.example.v2ourbook.models.Message;
import com.example.v2ourbook.models.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class MessageService {

    UserService userService;

    public MessageService(UserService userService) {
        this.userService = userService;
    }

    public String createMessage(String text, Long processId) throws ExecutionException, InterruptedException, ExceptionBlueprint {
        User currentUser = userService.getCurrentUser();
//        HashSet<GetBookProcess> getBookProcesses = new HashSet<>(currentUser.getGiveProcesses());
//        getBookProcesses.addAll(currentUser.getReceiveProcesses());
//        System.out.println("Ids: " + getBookProcesses.stream().map(GetBookProcess::getId).collect(Collectors.toList()).toString());
//        System.out.println("chatId: " + chatId);
//        if (getBookProcesses.stream().noneMatch(process -> process.getId().equals(chatId))){
//            throw new ExceptionBlueprint("user not authorized","no",1);
//        }
        return sendMessage(currentUser, text, processId, false);
    }

    public String createSystemMessage(String text, Long processId) throws ExecutionException, InterruptedException, ExceptionBlueprint {
//        HashSet<GetBookProcess> getBookProcesses = new HashSet<>(currentUser.getGiveProcesses());
//        getBookProcesses.addAll(currentUser.getReceiveProcesses());
//        System.out.println("Ids: " + getBookProcesses.stream().map(GetBookProcess::getId).collect(Collectors.toList()).toString());
//        System.out.println("chatId: " + chatId);
//        if (getBookProcesses.stream().noneMatch(process -> process.getId().equals(chatId))){
//            throw new ExceptionBlueprint("user not authorized","no",1);
//        }
        User system = userService.findByUsername("Reminder");
        return sendMessage(system, text, processId, true);
    }

    private String sendMessage(User user, String text, Long processId, boolean system) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Message newMessage = createFromRestMessage(text, processId);
        DocumentReference docRef = db.collection("chats").document(processId.toString()).collection("messages").document(newMessage.getMessage_id());
        Map<String, Object> data = new HashMap<>();
        data.put("_id", newMessage.getMessage_id());
        data.put("text", newMessage.getText());
        data.put("createdAt", newMessage.getCreatedAt());
        data.put("userId", user.getId());
        data.put("name", user.getUsername());
        if (system){
            data.put("system", true);
        }
        data.put("avatar", "https://static.vecteezy.com/ti/gratis-vektor/t2/550980-benutzer-icon-kostenlos-vektor.jpg");
        ApiFuture<WriteResult> result = docRef.set(data);
        return result.get().getUpdateTime().toString();
    }

    public Message createFromRestMessage(String text, Long processId){
        Message newMessage = new Message();
        newMessage.setText(text);
        return newMessage;
    }
}
