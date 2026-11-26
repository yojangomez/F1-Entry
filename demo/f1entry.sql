-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema F1Entry
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema F1Entry
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `F1Entry` ;
USE `F1Entry` ;

-- -----------------------------------------------------
-- Table `F1Entry`.`spectators`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `F1Entry`.`spectators` (
  `spectator_id` INT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(100) NOT NULL,
  `type_document` VARCHAR(100) NOT NULL,
  `document_number` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(30) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`spectator_id`),
  UNIQUE INDEX `numero documento_UNIQUE` (`document_number` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `F1Entry`.`Circuits`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `F1Entry`.`Circuits` (
  `circuit_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `location` VARCHAR(100) NOT NULL,
  `race_date` DATETIME NOT NULL,
  `base_price` DECIMAL(10,2) NOT NULL,
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`circuit_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `F1Entry`.`Viewing_zones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `F1Entry`.`Viewing_zones` (
  `zone_id` INT NOT NULL AUTO_INCREMENT,
  `circuit_id` INT NOT NULL,
  `zone_name` VARCHAR(100) NOT NULL,
  `zone_price` DECIMAL(10,2) NOT NULL,
  `total_capacity` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`zone_id`),
  INDEX `circuit_id_idx` (`circuit_id` ASC) VISIBLE,
  CONSTRAINT `circuit_id`
    FOREIGN KEY (`circuit_id`)
    REFERENCES `F1Entry`.`Circuits` (`circuit_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `F1Entry`.`Registrations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `F1Entry`.`Registrations` (
  `registration_id` INT NOT NULL AUTO_INCREMENT,
  `spectator_id` INT NOT NULL,
  `circuit_id` INT NOT NULL,
  `zone_id` INT NOT NULL,
  `total_paid` DECIMAL(10,2) NOT NULL,
  `status` ENUM('pending', 'confirmed', 'rejected', 'cancelled') NOT NULL DEFAULT 'pending',
  `registered_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`registration_id`),
  INDEX `idCategorias_idx` (`zone_id` ASC) VISIBLE,
  INDEX `idCircuitos_idx` (`circuit_id` ASC) VISIBLE,
  INDEX `idParticipante_idx` (`spectator_id` ASC) VISIBLE,
  CONSTRAINT `zoneid`
    FOREIGN KEY (`zone_id`)
    REFERENCES `F1Entry`.`Viewing_zones` (`zone_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `circuitid`
    FOREIGN KEY (`circuit_id`)
    REFERENCES `F1Entry`.`Circuits` (`circuit_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `spectatorid`
    FOREIGN KEY (`spectator_id`)
    REFERENCES `F1Entry`.`spectators` (`spectator_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `F1Entry`.`Payments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `F1Entry`.`Payments` (
  `payment_id` INT NOT NULL AUTO_INCREMENT,
  `registration_id` INT NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `payment_method` VARCHAR(50) NOT NULL,
  `transaction_reference` VARCHAR(100) NOT NULL,
  `payment_status` ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
  `is_refundable` TINYINT(1) NOT NULL DEFAULT 0,
  `payment_date` DATETIME NOT NULL,
  PRIMARY KEY (`payment_id`),
  UNIQUE INDEX `transaction_reference_UNIQUE` (`transaction_reference` ASC) VISIBLE,
  INDEX `registration_id_idx` (`registration_id` ASC) VISIBLE,
  CONSTRAINT `registration_id`
    FOREIGN KEY (`registration_id`)
    REFERENCES `F1Entry`.`Registrations` (`registration_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
