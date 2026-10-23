const studentForm = document.getElementById('studentForm');
const tableBody = document.getElementById('studentTableBody');

// Fetch and display all students
async function loadStudents() {
  try {
    const response = await fetch('http://localhost:8080/api/students');
    const students = await response.json();
    tableBody.innerHTML = '';
    
    students.forEach(student => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${student.id}</td>
        <td>${student.name}</td>
        <td>${student.email}</td>
        <td>${student.course}</td>
        <td>₹${student.fee}</td>
      `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.log('Waiting for Java Web Server connection...', error);
  }
}

// Add a student
studentForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  
  const student = {
    name: document.getElementById('name').value,
    email: document.getElementById('email').value,
    course: document.getElementById('course').value,
    fee: parseFloat(document.getElementById('fee').value)
  };

  try {
    await fetch('http://localhost:8080/api/students', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(student)
    });
    studentForm.reset();
    loadStudents();
  } catch (error) {
    console.error('Failed to save student:', error);
  }
});

// Load on start
loadStudents();