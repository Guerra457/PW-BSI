package br.edu.ifg.luziania.model.dto;

public class DashboardDTO {

    private int totalChamadosAbertos;
    private int totalChamadosFechados;
    private int totalChamadosPendentes;
    private int totalUsuariosAtivos;

    // Construtor vazio
    public DashboardDTO() {
    }

    // Construtor com parâmetros
    public DashboardDTO(int totalChamadosAbertos, int totalChamadosFechados, int totalChamadosPendentes, int totalUsuariosAtivos) {
        this.totalChamadosAbertos = totalChamadosAbertos;
        this.totalChamadosFechados = totalChamadosFechados;
        this.totalChamadosPendentes = totalChamadosPendentes;
        this.totalUsuariosAtivos = totalUsuariosAtivos;
    }

    // Getters e Setters
    public int getTotalChamadosAbertos() {
        return totalChamadosAbertos;
    }

    public void setTotalChamadosAbertos(int totalChamadosAbertos) {
        this.totalChamadosAbertos = totalChamadosAbertos;
    }

    public int getTotalChamadosFechados() {
        return totalChamadosFechados;
    }

    public void setTotalChamadosFechados(int totalChamadosFechados) {
        this.totalChamadosFechados = totalChamadosFechados;
    }

    public int getTotalChamadosPendentes() {
        return totalChamadosPendentes;
    }

    public void setTotalChamadosPendentes(int totalChamadosPendentes) {
        this.totalChamadosPendentes = totalChamadosPendentes;
    }

    public int getTotalUsuariosAtivos() {
        return totalUsuariosAtivos;
    }

    public void setTotalUsuariosAtivos(int totalUsuariosAtivos) {
        this.totalUsuariosAtivos = totalUsuariosAtivos;
    }
}
