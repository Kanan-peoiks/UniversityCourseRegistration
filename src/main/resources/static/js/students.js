const STUDENT_API = 'http://localhost:8080/api/students';
const COURSE_API = 'http://localhost:8080/api/courses';

window.onload = function() {
    loadStudents();
    loadCoursesForSelect();
    loadStudentsForSelect();
};

const form = document.getElementById('studentForm');
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = studentId.value;
    const student = {
        firstName: firstName.value,
        lastName: lastName.value,
        email: email.value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${STUDENT_API}/${id}` : STUDENT_API;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(student)
        });
        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || 'Xəta baş verdi');
        }
        form.reset();
        studentId.value = '';
        loadStudents();
        loadStudentsForSelect();
    } catch (error) {
        alert('Tələbə saxlanmadı: ' + error.message);
    }
});

async function loadStudents() {
    const list = document.getElementById('studentList');
    list.innerHTML = '';

    try {
        const res = await fetch(STUDENT_API);
        if (!res.ok) throw new Error('Tələbələr yüklənmədi');
        const students = await res.json();

        students.forEach(s => {
            const li = document.createElement('li');
            li.innerHTML = `
                <div>
                    ${s.firstName} ${s.lastName} (${s.email})
                </div>
                <div>
                    <button onclick="editStudent(${s.id})">Yenilə</button>
                    <button onclick="deleteStudent(${s.id})">Sil</button>
                </div>
            `;
            list.appendChild(li);
        });
    } catch (error) {
        alert('Tələbə siyahısı yüklənmədi: ' + error.message);
    }
}

function editStudent(id) {
    fetch(`${STUDENT_API}/${id}`)
        .then(res => res.json())
        .then(s => {
            studentId.value = s.id;
            firstName.value = s.firstName;
            lastName.value = s.lastName;
            email.value = s.email;
        }).catch(error => alert('Xəta: ' + error));
}

async function deleteStudent(id) {
    if (confirm('Silmək istəyirsən?')) {
        try {
            const res = await fetch(`${STUDENT_API}/${id}`, { method: 'DELETE' });
            if (!res.ok) throw new Error(await res.text());
            loadStudents();
            loadStudentsForSelect();
        } catch (error) {
            alert('Silinmədi: ' + error.message);
        }
    }
}

async function loadCoursesForSelect() {
    const select = document.getElementById('courseSelect');
    const selectRemove = document.getElementById('courseSelectRemove');
    select.innerHTML = '<option value="">Kurs seç</option>';
    selectRemove.innerHTML = '<option value="">Kurs seç</option>';

    try {
        const res = await fetch(COURSE_API);
        if (!res.ok) throw new Error('Kurslar yüklənmədi: ' + res.status);
        const courses = await res.json();

        courses.forEach(c => {
            const option = document.createElement('option');
            option.value = c.id;
            option.textContent = c.name;
            select.appendChild(option);
            selectRemove.appendChild(option.cloneNode(true));
        });
    } catch (error) {
        console.error(error);
        alert('Kurs select-i yüklənmədi: ' + error.message);
    }
}

async function loadStudentsForSelect() {
    const select = document.getElementById('studentSelect');
    const selectRemove = document.getElementById('studentSelectRemove');
    select.innerHTML = '<option value="">Tələbə seç</option>';
    selectRemove.innerHTML = '<option value="">Tələbə seç</option>';

    try {
        const res = await fetch(STUDENT_API);
        if (!res.ok) throw new Error('Tələbələr yüklənmədi: ' + res.status);
        const students = await res.json();

        students.forEach(s => {
            const option = document.createElement('option');
            option.value = s.id;
            option.textContent = `${s.firstName} ${s.lastName}`;
            select.appendChild(option);
            selectRemove.appendChild(option.cloneNode(true));
        });
    } catch (error) {
        console.error(error);
        alert('Tələbə select-i yüklənmədi: ' + error.message);
    }
}

async function addCourseToStudent() {
    const studentId = document.getElementById('studentSelect').value;
    const courseId = document.getElementById('courseSelect').value;

    if (!studentId || !courseId) {
        alert('Tələbə və kurs seçməlisiniz');
        return;
    }

    try {
        const res = await fetch(`${STUDENT_API}/${studentId}/courses/${courseId}`, { method: 'POST' });
        if (!res.ok) throw new Error(await res.text());
        alert('Kurs uğurla əlavə olundu!');
    } catch (error) {
        alert('Xəta: ' + error.message);
    }
}

async function removeCourseFromStudent() {
    const studentId = document.getElementById('studentSelectRemove').value;
    const courseId = document.getElementById('courseSelectRemove').value;

    if (!studentId || !courseId) {
        alert('Tələbə və kurs seçməlisiniz');
        return;
    }

    try {
        const res = await fetch(`${STUDENT_API}/${studentId}/courses/${courseId}`, { method: 'DELETE' });
        if (!res.ok) throw new Error(await res.text());
        alert('Kurs uğurla silindi!');
    } catch (error) {
        alert('Xəta: ' + error.message);
    }
}