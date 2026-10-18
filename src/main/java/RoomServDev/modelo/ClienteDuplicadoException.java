package RoomServDev.modelo;

//*********extends Exception*********

public class ClienteDuplicadoException extends Exception{

    //********* Constructor *********

    public ClienteDuplicadoException(String mensaje){
        // ********* pasamos el mensaje al constructor del padre (Exception) *********
        super(mensaje)
    }

}