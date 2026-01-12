const API = "http://localhost:8080/api/students";

const studentList = document.getElementById("studentList");
const form = document.getElementById("studentForm");

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const student = {
        firstName: firstName.value,
        lastName: lastName.value,
        email: email.value
    };

    await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(student)
    });

    form.reset();
    loadStudents();
});

async function loadStudents() {
    studentList.innerHTML = "";

    const res = await fetch(API);
    const students = await res.json();

    students.forEach(s => {
        const li = document.createElement("li");
        li.innerText = `${s.firstName} ${s.lastName} (${s.email})`;
        studentList.appendChild(li);
    });
}

loadStudents();
