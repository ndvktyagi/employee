package com.digithon.hr.entity;

import com.digithon.hr.security.PiEncryptedStringConverter;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees", uniqueConstraints = @UniqueConstraint(name = "uk_employees_email", columnNames = "email"))
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    // PI Co-Pilot: encrypted copy of "first_name". Written on every save; firstName is read from it.
    @Column(name = "first_name_enc")
    @Convert(converter = PiEncryptedStringConverter.class)
    private String firstNameEncrypted;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 180)
    private String email;

    // PI Co-Pilot: encrypted copy of "email". Written on every save; email is read from it.
    @Column(name = "email_enc")
    @Convert(converter = PiEncryptedStringConverter.class)
    private String emailEncrypted;

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

    // PI Co-Pilot: keeps the encrypted copies of personal data current.
    @PrePersist
    @PreUpdate
    void piCopilotEncrypt() {
        this.emailEncrypted = this.email; // PI Co-Pilot
        this.firstNameEncrypted = this.firstName; // PI Co-Pilot
    }

    // PI Co-Pilot: personal data is read from the encrypted copies.
    @PostLoad
    void piCopilotDecrypt() {
        if (this.emailEncrypted != null) this.email = this.emailEncrypted; // PI Co-Pilot
        if (this.firstNameEncrypted != null) this.firstName = this.firstNameEncrypted; // PI Co-Pilot
    }
}