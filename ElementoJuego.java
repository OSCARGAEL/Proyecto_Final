// Clase abstracta para representarel nombre
abstract class ElementoJuego {
    protected String nombre; // Nombre del objeto

    public ElementoJuego(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
