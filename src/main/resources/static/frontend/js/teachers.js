const API = "http://localhost:8080/api/teachers";
const teacherList = document.getElementById("teacherList");
const form = document.getElementById("teacherForm");

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const teacher = {
        firstName: firstName.value,
        lastName: lastName.value,
        department: department.value
    };

    await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(teacher)
    });

    form.reset();
    loadTeachers();
});

async function loadTeachers() {
    teacherList.innerHTML = "";

    const res = await fetch(API);
    const teachers = await res.json();

    teachers.forEach(t => {
        const li = document.createElement("li");
        li.innerText = `${t.firstName} ${t.lastName} - ${t.department}`;
        teacherList.appendChild(li);
    });
}

loadTeachers();
