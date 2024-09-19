package br.edu.ifg.luziania.model.dto;

public class ResponseDTO {
    private String message;

    // Construtor padrão
    public ResponseDTO() {}

    // Construtor com mensagem
    public ResponseDTO(String message) {
        this.message = message;
    }

    // Getter para a mensagem
    public String getMessage() {
        return message;
    }

    // Setter para a mensagem
    public void setMessage(String message) {
        this.message = message;
    }
}
