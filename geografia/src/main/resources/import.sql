-- 1. Insertar el Pais (id_pais sera 1 automaticamente)
INSERT INTO pais (nombre_pais) VALUES ('Chile');

-- 2. Insertar Regiones (Ligadas al id_pais = 1)
INSERT INTO region (nombre_region, id_pais) VALUES ('Region Metropolitana de Santiago', 1);
INSERT INTO region (nombre_region, id_pais) VALUES ('Region de Valparaiso', 1);
INSERT INTO region (nombre_region, id_pais) VALUES ('Region del Biobio', 1);

-- 3. Insertar Ciudades (Ligadas a sus respectivas regiones)
-- Ciudades en RM (id_region = 1) -> id_ciudad sera 1 y 2
INSERT INTO ciudad (nombre_ciudad, id_region) VALUES ('Santiago', 1);
INSERT INTO ciudad (nombre_ciudad, id_region) VALUES ('Cordillera', 1);
-- Ciudades en Valparaiso (id_region = 2) -> id_ciudad sera 3 y 4
INSERT INTO ciudad (nombre_ciudad, id_region) VALUES ('Valparaiso', 2);
INSERT INTO ciudad (nombre_ciudad, id_region) VALUES ('Marga Marga', 2);
-- Ciudades en Biobio (id_region = 3) -> id_ciudad sera 5
INSERT INTO ciudad (nombre_ciudad, id_region) VALUES ('Concepcion', 3);

-- 4. Insertar Comunas (Ligadas a sus respectivas ciudades)
-- Comunas en Santiago (id_ciudad = 1)
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Santiago Centro', 1);
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Providencia', 1);
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Las Condes', 1);
-- Comunas en Cordillera (id_ciudad = 2)
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Puente Alto', 2);
-- Comunas en Valparaiso (id_ciudad = 3)
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Valparaiso', 3);
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Vina del Mar', 3);
-- Comunas en Marga Marga (id_ciudad = 4)
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Quilpue', 4);
-- Comunas en Concepcion (id_ciudad = 5)
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Concepcion Centro', 5);
INSERT INTO comuna (nombre_comuna, id_ciudad) VALUES ('Talcahuano', 5);