const COURSE_API = 'http://localhost:8080/api/courses';
const TEACHER_API = 'http://localhost:8080/api/teachers';

window.onload = function() {
    loadCourses();
    loadCoursesForSelect();       // Müəllim təyin və xal vermə üçün kurs select-i
    loadTeachersForSelect();      // Müəllim təyin üçün müəllim select-i
};

const form = document.getElementById('courseForm');
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = courseId.value;
    const course = {
        name: name.value,
        credits: credits.value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${COURSE_API}/${id}` : COURSE_API;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(course)
        });
        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || 'Kurs saxlanmadı');
        }
        form.reset();
        courseId.value = '';
        loadCourses();
        loadCoursesForSelect();
    } catch (error) {
        alert('Xəta: ' + error.message);
    }
});

async function loadCourses() {
    const list = document.getElementById('courseList');
    list.innerHTML = '';

    try {
        const res = await fetch(COURSE_API);
        if (!res.ok) throw new Error('Kurslar yüklənmədi: ' + res.status);
        const courses = await res.json();

        courses.forEach(c => {
            const li = document.createElement('li');
            li.innerHTML = `
                <div>
                    <strong>${c.name}</strong> (${c.credits} kredit, Xal: ${c.rating ? c.rating.toFixed(2) : '0.00'})<br>
                    Müəllim: ${c.teacher ? c.teacher.firstName + ' ' + c.teacher.lastName : 'Yoxdur'}
                </div>
                <div>
                    <button onclick="editCourse(${c.id})">Yenilə</button>
                    <button onclick="deleteCourse(${c.id})">Sil</button>
                </div>
            `;
            list.appendChild(li);
        });
    } catch (error) {
        console.error(error);
        alert('Kurs siyahısı yüklənmədi: ' + error.message);
    }
}

function editCourse(id) {
    fetch(`${COURSE_API}/${id}`)
        .then(res => res.json())
        .then(c => {
            courseId.value = c.id;
            name.value = c.name;
            credits.value = c.credits;
        })
        .catch(error => alert('Xəta: ' + error));
}

async function deleteCourse(id) {
    if (confirm('Bu kursu silmək istəyirsən?')) {
        try {
            const res = await fetch(`${COURSE_API}/${id}`, { method: 'DELETE' });
            if (!res.ok) throw new Error(await res.text());
            loadCourses();
            loadCoursesForSelect();
        } catch (error) {
            alert('Silinmədi: ' + error.message);
        }
    }
}

async function loadCoursesForSelect() {
    const selectAssign = document.getElementById('courseSelectAssign');
    const selectRate = document.getElementById('courseSelectRate');
    selectAssign.innerHTML = '<option value="">Kurs seç</option>';
    selectRate.innerHTML = '<option value="">Kurs seç</option>';

    try {
        const res = await fetch(COURSE_API);
        if (!res.ok) throw new Error('Kurslar yüklənmədi: ' + res.status);
        const courses = await res.json();

        courses.forEach(c => {
            const option = document.createElement('option');
            option.value = c.id;
            option.textContent = c.name;
            selectAssign.appendChild(option);
            selectRate.appendChild(option.cloneNode(true));
        });
    } catch (error) {
        console.error(error);
        alert('Kurs select-i yüklənmədi: ' + error.message);
    }
}

async function loadTeachersForSelect() {
    const select = document.getElementById('teacherSelectAssign');
    select.innerHTML = '<option value="">Müəllim seç</option>';

    try {
        const res = await fetch(TEACHER_API);
        if (!res.ok) throw new Error('Müəllimlər yüklənmədi: ' + res.status);
        const teachers = await res.json();

        teachers.forEach(t => {
            const option = document.createElement('option');
            option.value = t.id;
            option.textContent = `${t.firstName} ${t.lastName}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error(error);
        alert('Müəllim select-i yüklənmədi: ' + error.message);
    }
}

async function assignTeacherToCourse() {
    const courseId = document.getElementById('courseSelectAssign').value;
    const teacherId = document.getElementById('teacherSelectAssign').value;

    if (!courseId || !teacherId) {
        alert('Kurs və müəllim seçməlisiniz');
        return;
    }

    try {
        const res = await fetch(`${COURSE_API}/${courseId}/teacher/${teacherId}`, { method: 'POST' });
        if (!res.ok) throw new Error(await res.text());
        alert('Müəllim uğurla təyin olundu!');
        loadCourses();
    } catch (error) {
        alert('Xəta: ' + error.message);
    }
}

async function rateCourse() {
    const courseId = document.getElementById('courseSelectRate').value;
    const ratingValue = document.getElementById('rating').value;

    if (!courseId || !ratingValue) {
        alert('Kurs və xal seçməlisiniz');
        return;
    }

    try {
        const res = await fetch(`${COURSE_API}/${courseId}/rate`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(parseFloat(ratingValue))
        });
        if (!res.ok) throw new Error(await res.text());
        alert('Xal uğurla verildi!');
        loadCourses();
    } catch (error) {
        alert('Xəta: ' + error.message);
    }
}