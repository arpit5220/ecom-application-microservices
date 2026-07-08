package com.ecommerce.user.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User {
       @Id
       private String id;
       private String firstName;
       private String lastName;

       @Indexed(unique = true)
       private String email;
       private String phone;
       private UserRole role = UserRole.CUSTOMER;
       private Address address;
       @CreatedBy
       private LocalDateTime createdAt;
       @LastModifiedDate
       private LocalDateTime updatedAt;
}
