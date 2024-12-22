package com.get_offer.chat.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * 1. 로그인
 * 2. 채팅방 생성
 * 3. 해당 api 호출 (과거 내역 조회 api 호출, 웹소켓 호출)
 */
/** Test SQL
 *
 * INSERT INTO USERS (ID, NICKNAME, IMAGE, CREATED_AT, UPDATED_AT)
 * VALUES (2, 'test2', 'https://drive.google.com/file/d/1R9EIOoEWWgPUhY6e-t4VFuqMgknl7rm8/view?usp=sharing',
 *         '2024-01-01 00:00:00', '2024-01-01 00:00:00');
 *
 * INSERT INTO PRODUCTS (ID, WRITER_ID, TITLE, DESCRIPTION, CATEGORY, CURRENT_PRICE, START_PRICE, STATUS, START_DATE,
 *                       END_DATE, CREATED_AT, UPDATED_AT, IMAGES)
 * VALUES (1, 2, 'nintendo', '닌텐도 새 제품', 'GAMES', 10000, 5000, 'IN_PROGRESS', '2024-01-02 00:00:00', '2024-01-04 00:00:00',
 *         '2024-01-02 00:00:00', '2024-01-02 00:00:00',
 *         '{"images":["https://picsum.photos/200/300","https://picsum.photos/200/300"]}');
 *
 */
@Controller
class ChatFrontController {
    @GetMapping("/chat/{roomId}/{isBuyer}")
    fun checkout(
        @PathVariable roomId: String,
        @PathVariable isBuyer: String,
        model: Model
    ): String {
        if (isBuyer == "Y") {
            model.addAttribute("userId", 1)
            model.addAttribute("receiver", "seller")
            model.addAttribute("roomId", roomId)
        } else {
            model.addAttribute("userId", 2)
            model.addAttribute("receiver", "buyer")
            model.addAttribute("roomId", roomId)
        }

        return "chatRoom"
    }

    @GetMapping("/group-chat/{roomId}/{userId}")
    fun groupChatCheckout(
//        @AuthenticatedUser userId: Long,
        @PathVariable roomId: String,
        @PathVariable userId: Long,
        model: Model,
    ): String {
        model.addAttribute("productId", roomId)
        model.addAttribute("senderId", userId)

        return "groupChat"
    }
}