const API = "http://localhost:8080/api/courses";
const courseList = document.getElementById("courseList");
const form = document.getElementById("courseForm");

form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const course = {
        name: name.value,
        credits: credits.value
    };

    await fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(course)
    });

    form.reset();
    loadCourses();
});

async function loadCourses() {
    courseList.innerHTML = "";

    const res = await fetch(API);
    const courses = await res.json();

    courses.forEach(c => {
        const li = document.createElement("li");
        li.innerText = `${c.name} (${c.credits} credits)`;
        courseList.appendChild(li);
    });
}

loadCourses();
