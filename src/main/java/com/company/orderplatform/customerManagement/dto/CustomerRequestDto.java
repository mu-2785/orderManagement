package com.company.orderplatform.customerManagement.dto;


import com.company.orderplatform.customerManagement.entity.CustomerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerRequestDto {

    @NotBlank(message = "Name can not be blank")
    @Size(max = 150 , message = "Name can not be greater then 150 char")
    private String name;
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Please provide a valid email address")
    @Size(max = 250 , message = "Email can not be greater then 250 char")
    private String email;
    @NotNull(message = "Status can not be blank")
    private CustomerStatus status;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomerRequestDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
}
