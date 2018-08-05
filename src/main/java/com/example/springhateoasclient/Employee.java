package com.example.springhateoasclient;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employee {
  private long id;
  private String firstName;
  private String lastName;
  private String role;
}
