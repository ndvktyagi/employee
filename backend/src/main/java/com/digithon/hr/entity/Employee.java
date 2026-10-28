package com.digithon.hr.entity;

import com.digithon.hr.security.PiEncryptedStringConverter;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // PI Co-Pilot: stored encrypted in "first_name_enc"; the plain column "first_name" is retired.
    @Column(name = "first_name_enc", nullable = false)
    @Convert(converter = PiEncryptedStringConverter.class)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    // PI Co-Pilot: stored encrypted in "email_enc"; the plain column "email" is retired.
    @Column(name = "email_enc", nullable = false)
    @Convert(converter = PiEncryptedStringConverter.class)
    private String email;

    // Personal data used for the future DPDP scanning demonstration.
    @Column(length = 25)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false, foreignKey = @ForeignKey(name = "fk_employees_department"))
    private Department department;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    protected Employee() { }

    public Employee(String firstName, String lastName, String email, String phone, Department department, LocalDate joiningDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.joiningDate = joiningDate;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Department getDepartment() { return department; }
    public LocalDate getJoiningDate() { return joiningDate; }

    public void update(String firstName, String lastName, String email, String phone, Department department, LocalDate joiningDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.joiningDate = joiningDate;
    }
}