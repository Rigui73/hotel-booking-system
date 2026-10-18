public class Cliente {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String dni;
    private int telefono;
    private String email;

    public Cliente() {
    }

    public Cliente(String string, String string2, String string3, String string4, int n, String string5) {
        this.nombre = string;
        this.apellido1 = string2;
        this.apellido2 = string3;
        this.dni = string4;
        this.telefono = n;
        this.email = string5;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String string) {
        this.nombre = string;
    }

    public String getApellido1() {
        return this.apellido1;
    }

    public void setApellido1(String string) {
        this.apellido1 = string;
    }

    public String getApellido2() {
        return this.apellido2;
    }

    public void setApellido2(String string) {
        this.apellido2 = string;
    }

    public String getDni() {
        return this.dni;
    }

    public void setDni(String string) {
        this.dni = string;
    }

    public int getTelefono() {
        return this.telefono;
    }

    public void setTelefono(int n) {
        this.telefono = n;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String string) {
        this.email = string;
    }

    public String toString() {
        return "Cliente{nombre='" + this.nombre + "', apellido1='" + this.apellido1 + "', apellido2='" + this.apellido2 + "', dni='" + this.dni + "', telefono=" + this.telefono + ", email='" + this.email + "'}";
    }
}
