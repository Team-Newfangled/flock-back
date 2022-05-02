package com.newfangled.flockbackend.domain.chat.repository;

import com.newfangled.flockbackend.domain.account.entity.Account;
import com.newfangled.flockbackend.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    List<ChatRoom> findChatRoomByCustomer(Account account);

    List<ChatRoom> findChatRoomsByStore(Account account);
}
