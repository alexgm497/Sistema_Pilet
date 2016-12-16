-- Funcion para slipt de Bitacora
CREATE FUNCTION SPLIT_STR(
  x VARCHAR(255),
  delim VARCHAR(12),
  pos INT
)
RETURNS VARCHAR(255) DETERMINISTIC
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
       delim, '');
       

-- TRIGGER PARA INSERT BITACORA
DELIMITER // 
CREATE TRIGGER BITACORA_BI_TRIGGER 
BEFORE INSERT ON bitacora 
FOR EACH ROW 
BEGIN 
	SET NEW.codi_usua = CONVERT(SPLIT_STR(NEW.acci_bita,'-',1),UNSIGNED INTEGER);
	SET NEW.nomb_bita = SPLIT_STR(NEW.acci_bita, '-', 2);
	SET NEW.acci_bita = SPLIT_STR(NEW.acci_bita, '-', 3);
END//

-- Funcion para obtener el Path completo

delimiter //
CREATE FUNCTION GET_URL_PATH(codi int)
RETURNS VARCHAR(1000) DETERMINISTIC
BEGIN
	DECLARE path VARCHAR(1000);
	DECLARE temp VARCHAR(1000);
	DECLARE padre int;
	SET path = "";
	SELECT p.dire_perm, p.refe_perm INTO path, padre FROM permiso p WHERE p.codi_perm = codi;
	WHILE(padre <> 0) DO
	SELECT p.dire_perm, p.refe_perm INTO temp, padre FROM permiso p WHERE p.codi_perm = padre;
	SET path = CONCAT(temp,path);
     END WHILE;
RETURN path;
END; //
delimiter ;

DELIMITER //
CREATE TRIGGER `resoSoli` AFTER INSERT ON `resolucion_solicitudes` FOR EACH ROW BEGIN
DECLARE id_soli integer;
DECLARE fech_hora_soli datetime;
SET @id_soli = (SELECT so.codi_soli FROM solicitudes so INNER JOIN resolucion_solicitudes rs ON rs.codi_soli = so.codi_soli WHERE rs.codi_reso_soli = NEW.codi_reso_soli);
SET @fech_hora_soli = (SELECT sol.fech_hora_soli FROM solicitudes sol WHERE sol.codi_soli = @id_soli);
UPDATE solicitudes s SET s.esta_soli = 3, s.tiem_reso_soli = (SELECT DATEDIFF(NEW.fech_reso_soli, @fech_hora_soli)) WHERE s.codi_soli = @id_soli;
END//
DELIMITER ;

DELIMITER //
CREATE TRIGGER `finaSoli` AFTER INSERT ON `evaluacion_resoluciones` FOR EACH ROW BEGIN
DECLARE id_soli integer;
SET @id_soli = (SELECT so.codi_soli FROM solicitudes so INNER JOIN resolucion_solicitudes rs ON rs.codi_soli = so.codi_soli INNER JOIN evaluacion_resoluciones er ON er.codi_reso_soli = rs.codi_reso_soli WHERE er.codi_reso_soli = NEW.codi_reso_soli);
IF (NEW.punt_eval_reso >= 4) THEN
UPDATE solicitudes s SET s.esta_soli = 4 WHERE s.codi_soli = @id_soli;
ELSE
UPDATE solicitudes s SET s.esta_soli = 2 WHERE s.codi_soli = @id_soli;
END IF;
END//
DELIMITER ;