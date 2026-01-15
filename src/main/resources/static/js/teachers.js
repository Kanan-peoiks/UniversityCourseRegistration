const TEACHER_API = 'http://localhost:8080/api/teachers';

window.onload = function() {
    loadTeachers();
};

const form = document.getElementById('teacherForm');
form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = teacherId.value;
    const teacher = {
        firstName: firstName.value,
        lastName: lastName.value,
        department: department.value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${TEACHER_API}/${id}` : TEACHER_API;

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(teacher)
        });
        if (!res.ok) {
            const errorText = await res.text();
            throw new Error(errorText || 'Müəllim saxlanmadı');
        }
        form.reset();
        teacherId.value = '';
        loadTeachers();
    } catch (error) {
        alert('Xəta: ' + error.message);
    }
});

async function loadTeachers() {
    const list = document.getElementById('teacherList');
    list.innerHTML = '';

    try {
        const res = await fetch(TEACHER_API);
        if (!res.ok) throw new Error('Müəllimlər yüklənmədi: ' + res.status);
        const teachers = await res.json();

        teachers.forEach(t => {
            const li = document.createElement('li');
            li.innerHTML = `
                <div>
                    ${t.firstName} ${t.lastName} (${t.department})
                </div>
                <div>
                    <button onclick="editTeacher(${t.id})">Yenilə</button>
                    <button onclick="deleteTeacher(${t.id})">Sil</button>
                </div>
            `;
            list.appendChild(li);
        });
    } catch (error) {
        console.error(error);
        alert('Müəllim siyahısı yüklənmədi: ' + error.message);
    }
}

function editTeacher(id) {
    fetch(`${TEACHER_API}/${id}`)
        .then(res => res.json())
        .then(t => {
            teacherId.value = t.id;
            firstName.value = t.firstName;
            lastName.value = t.lastName;
            department.value = t.department;
        })
        .catch(error => alert('Xəta: ' + error));
}

async function deleteTeacher(id) {
    if (confirm('Bu müəllimi silmək istəyirsən?')) {
        try {
            const res = await fetch(`${TEACHER_API}/${id}`, { method: 'DELETE' });
            if (!res.ok) throw new Error(await res.text());
            loadTeachers();
        } catch (error) {
            alert('Silinmədi: ' + error.message);
        }
    }
}