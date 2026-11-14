USE roomservdev_db;

DELIMITER $$
CREATE PROCEDURE sp_comprobar_solapamiento_reserva(
    IN p_id_habitacion INT,
    IN p_fecha_entrada DATE,
    IN p_fecha_salida DATE,
    IN p_id_reserva_ignorar INT
)
BEGIN
    SELECT id_reserva, fecha_entrada, fecha_salida
    FROM reservas
    WHERE estado = 1
      AND id_habitacion = p_id_habitacion
      AND fecha_entrada < p_fecha_salida
      AND fecha_salida > p_fecha_entrada
      AND (p_id_reserva_ignorar IS NULL OR id_reserva <> p_id_reserva_ignorar)
    ORDER BY fecha_entrada
    LIMIT 1;
END$$
DELIMITER ;
