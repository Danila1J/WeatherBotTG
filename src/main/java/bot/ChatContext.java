package bot;

/**
 * Класс ChatContext предназначен для хранения информации, связанной с чатом.
 */
public class ChatContext {
    private String chatId;
    private Integer firstMessageId;

    public ChatContext(String chatId, Integer firstMessageId) {
        this.chatId = chatId;
        this.firstMessageId = firstMessageId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Integer getFirstMessageId() {
        return firstMessageId;
    }

    public void setFirstMessageId(Integer firstMessageId) {
        this.firstMessageId = firstMessageId;
    }
}
