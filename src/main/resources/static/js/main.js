window.onload = function() {
    fetchPopularCourses();
};

function fetchPopularCourses() {
    fetch('http://localhost:8080/api/courses/popular')
        .then(response => {
            if (!response.ok) throw new Error('Populyar kurslar yüklənmədi: ' + response.status);
            return response.json();
        })
        .then(data => {
            const list = document.getElementById('course-list');
            list.innerHTML = '';

            if (data.length === 0) {
                list.innerHTML = '<li>Hələ populyar kurs yoxdur</li>';
            } else {
                data.forEach(course => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <div>
                            <strong>${course.name}</strong> (Kredit: ${course.credits}, Xal: ${course.rating ? course.rating.toFixed(2) : '0.00'})<br>
                            Müəllim: ${course.teacher ? course.teacher.firstName + ' ' + course.teacher.lastName : 'Təyin olunmayıb'}
                        </div>
                    `;
                    list.appendChild(li);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Populyar kurslar yüklənmədi: ' + error.message);
        });
}