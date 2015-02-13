package com.excilys.computerdatabase.persistence;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocalDatePersistenceConverter implements AttributeConverter<LocalDate, java.sql.Date> {

  @Override
  public java.sql.Date convertToDatabaseColumn(final LocalDate entityValue) {
    if (entityValue != null) {
      return java.sql.Date.valueOf(entityValue);
    }
    return null;
  }

  @Override
  public LocalDate convertToEntityAttribute(final java.sql.Date databaseValue) {
    if (databaseValue != null) {
      return databaseValue.toLocalDate();
    }
    return null;
  }
}