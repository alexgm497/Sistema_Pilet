DROP DATABASE IF EXISTS `sistemas_pilet`;
CREATE DATABASE IF NOT EXISTS `sistemas_pilet`

DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;

USE sistemas_pilet; 

CREATE TABLE IF NOT EXISTS `ROL`
(
    `codi_role` INT NOT NULL AUTO_INCREMENT,
    `nomb_role` VARCHAR(25) NOT NULL,
    `desc_role` VARCHAR(250) NOT NULL,
    `esta_role` INT NOT NULL,
    UNIQUE(nomb_role),
    PRIMARY KEY(codi_role)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE IF NOT EXISTS `PERMISO`
(
    `codi_perm` INT NOT NULL AUTO_INCREMENT,
    `desc_perm` VARCHAR(150) NOT NULL,
    `dire_perm` VARCHAR(150) NOT NULL,
    `refe_perm` INT,
    `esta_perm` INT NOT NULL,
    PRIMARY KEY(codi_perm)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `PERMISO_ROL`
(
    `codi_perm_role` INT NOT NULL AUTO_INCREMENT,
    `codi_perm` INT NOT NULL,
    `codi_role` INT NOT NULL,
    `esta_perm_role` INT NOT NULL,
    PRIMARY KEY(codi_perm_role)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


CREATE TABLE IF NOT EXISTS `USUARIO`
(
    `codi_usua` INT NOT NULL AUTO_INCREMENT,
    `acce_usua`  VARCHAR(150) NOT NULL,
    `esta_usua` INT NOT NULL,
    PRIMARY KEY(codi_usua)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `USUARIO_ROL`
(
    `codi_usua_role` INT NOT NULL AUTO_INCREMENT,
    `codi_role` INT NOT NULL,
    `codi_usua` INT NOT NULL,
    `esta_usua_role` INT NOT NULL,
    PRIMARY KEY(codi_usua_role)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `BITACORA`
(
    `codi_bita` INT NOT NULL AUTO_INCREMENT,
     `codi_usua` INT NULL,
     `nive_bita` VARCHAR(10) NOT NULL,
     `logg_bita` VARCHAR(50) NOT NULL,
     `nomb_bita` VARCHAR(100) NULL,
     `acci_bita` VARCHAR(1000) NOT NULL,
     `fech_bita` VARCHAR(50) NOT NULL,
     PRIMARY KEY(codi_bita)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `NOTIFICACION`(
    `codi_noti` INT NOT NULL AUTO_INCREMENT,
    `codi_usua` INT NOT NULL,
    `mens_noti` VARCHAR(500) NOT NULL,
    `esta_noti` INT NOT NULL,
    `path_noti` VARCHAR(150),
    `modu_noti` VARCHAR(200) NOT NULL,
PRIMARY KEY(codi_noti)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE `PERMISO` ADD CONSTRAINT `fk_codi_perm` FOREIGN KEY (`refe_perm`) REFERENCES `PERMISO` (`codi_perm`);
ALTER TABLE `USUARIO_ROL` ADD CONSTRAINT `fk_codi_role_usua` FOREIGN KEY (`codi_role`) REFERENCES `ROL` (`codi_role`);
ALTER TABLE `USUARIO_ROL` ADD CONSTRAINT `fk_codi_usua_role` FOREIGN KEY (`codi_usua`) REFERENCES `USUARIO` (`codi_usua`);
ALTER TABLE `BITACORA` ADD CONSTRAINT `fk_codi_usua_bita` FOREIGN KEY (`codi_usua`) REFERENCES `USUARIO` (`codi_usua`);
ALTER TABLE `NOTIFICACION` ADD CONSTRAINT `fk_codi_usua_noti` FOREIGN KEY (`codi_usua`) REFERENCES `USUARIO` (`codi_usua`);
ALTER TABLE `PERMISO_ROL` ADD CONSTRAINT `fk_codi_perm_role` FOREIGN KEY (`codi_perm`) REFERENCES `PERMISO` (`codi_perm`);
ALTER TABLE `PERMISO_ROL` ADD CONSTRAINT `fk_codi_role_perm` FOREIGN KEY (`codi_role`) REFERENCES `ROL` (`codi_role`);

INSERT INTO rol values(1,'Super Administrador','el mero mero',1);
INSERT INTO rol values(2,'Administrador','el mero sub',1);
INSERT INTO rol values(3,'Docente','el algo',1);
INSERT INTO rol values(4,'Secretaria','el ayno',1);

INSERT INTO usuario values(1,'demo@ricaldone.edu.sv',1);

INSERT INTO usuario_rol values(1,1,1,1);

INSERT INTO `permiso` (`codi_perm`, `desc_perm`, `dire_perm`, `refe_perm`, `esta_perm`) VALUES
(1, 'App', '/Sistema_Pilet/poo', NULL, 1),
(2, 'Modulo_usuarios', '/Modulo_usuarios', 1, 1),
(3, 'Roles', '/roles.xhtml', 2, 1),
(4, 'Usuario Rol', '/usuariorol.xhtml', 2, 1),
(5, 'Permiso Rol', '/permisorol.xhtml', 2, 1),
(6, 'Agregar', '/agregar', 3, 1),
(7, 'Modificar', '/modificar', 3, 1),
(8, 'Cambiar estado', '/estado', 3, 1);

INSERT INTO `permiso_rol` (`codi_perm_role`, `codi_perm`, `codi_role`, `esta_perm_role`) VALUES
(1, 1, 1, 1),
(2, 5, 1, 1),
(3, 3, 1, 1),
(4, 4, 1, 1),
(5, 2, 3, 1),
(6, 6, 1, 1),
(7, 7, 1, 1),
(8, 8, 1, 1);

DROP FUNCTION IF EXISTS SPLIT_STR;
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

DROP FUNCTION IF EXISTS GET_URL_PATH;
delimiter //
CREATE FUNCTION IF NOT EXISTS GET_URL_PATH (codi int)
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