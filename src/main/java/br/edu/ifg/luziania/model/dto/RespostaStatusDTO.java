package br.edu.ifg.luziania.model.dto;

public class RespostaStatusDTO {
    private int statusResposta;
    private String mensagem;
    private String url;

    public RespostaStatusDTO() {}
    public RespostaStatusDTO(int status, String mensagem, String url) {
        this.statusResposta = status;
        this.mensagem = mensagem;
        this.url = url;
    }

    public int getStatusResposta() {
        return statusResposta;
    }

    public void setStatusResposta(int statusResposta) {
        this.statusResposta = statusResposta;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
