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

    // PI Co-Pilot: stored encrypted in "first_name_enc"; the plain column "first_name" is retired.
    @Column(name = "first_name_enc", nullable = false)
    @Convert(converter = PiEncryptedStringConverter.class)
    private String firstName;

    // PI Co-Pilot: stored encrypted in "last_name_enc"; the plain column "last_name" is retired.
    @Column(name = "last_name_enc", nullable = false)
    @Convert(converter = PiEncryptedStringConverter.class)
    private String lastName;

    @Column(nullable = false, length = 180)
    private String email;

    // PI Co-Pilot: encrypted copy of "email". Read-only for Hibernate, so existing saves are unchanged;
    // it is written on every save and read only while encrypted read is on.
    @Column(name = "email_enc", insertable = false, updatable = false)
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
    }

    // PI Co-Pilot: personal data is read from the encrypted copies.
    @PostLoad
    void piCopilotDecrypt() {
        if (this.emailEncrypted != null) this.email = this.emailEncrypted; // PI Co-Pilot
    }
}