package com.newfangled.flockbackend.domain.chat.entity;

import com.newfangled.flockbackend.domain.account.entity.Account;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class ChatRoom implements Serializable {

    @Id
    @Column(name = "chatroom_id")
    private String id;

    private String name;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account customer;

    @OneToOne
    @JoinColumn(name = "store_id")
    private Account store;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public static ChatRoom createChatRoom(String name, Account customer, Account store) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.id = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.customer = customer;
        chatRoom.store = store;
        return chatRoom;
    }

    public void addChatMessages(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
    }
}
