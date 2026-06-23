/* Shared frontend script for all pages. Uses fetch() to call backend at http://localhost:8080
   Behaviour depends on body's data-page attribute. */

const API_BASE = 'https://coursework-deadline-optimiser.onrender.com/api';

function showLoading() { document.getElementById('loading')?.classList.remove('hidden'); }
function hideLoading() { document.getElementById('loading')?.classList.add('hidden'); }

// ---------- Auth ----------
function getToken() {
  return localStorage.getItem('jwt_token');
}

function redirectToLogin() {
  window.location.href = '/login.html';
}

function logout() {
  localStorage.removeItem('jwt_token');
  redirectToLogin();
}

// Redirect to login if no token (skip on auth pages)
(function checkAuth() {
  const page = document.body.dataset.page;
  if (page === 'login' || page === 'register') return;
  if (!getToken()) redirectToLogin();
})();

async function fetchJSON(url, opts = {}){
  showLoading();
  try{
    const token = getToken();
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const res = await fetch(url, { headers, ...opts });

    if (res.status === 401 || res.status === 403) {
      redirectToLogin();
      return;
    }
    if(!res.ok){
      const text = await res.text();
      throw new Error(`${res.status} ${res.statusText} - ${text}`);
    }
    const ct = res.headers.get('content-type') || '';
    if(ct.includes('application/json')){
      return await res.json();
    }
    return null;
  }catch(err){
    console.error('Fetch error', err);
    alert('Error: ' + err.message);
    throw err;
  }finally{
    hideLoading();
  }
}

// ---------- Students page ----------
async function loadStudents(){
  const tbody = document.querySelector('#students-table tbody');
  tbody.innerHTML = '';
  try{
    const students = await fetchJSON(`${API_BASE}/students`);
    students.forEach(s => {
      const tr = document.createElement('tr');
      tr.innerHTML = `<td>${s.id ?? ''}</td>
                      <td>${escapeHtml(s.name ?? '')}</td>
                      <td>${escapeHtml(s.email ?? '')}</td>
                      <td>${s.maxHoursPerDay ?? ''}</td>
                      <td class="actions">
                        <button class="btn outline" data-action="delete" data-id="${s.id}">Delete</button>
                      </td>`;
      tbody.appendChild(tr);
    });
  }catch(e){ console.warn(e); }
}

async function addStudent(e){
  e.preventDefault();
  const name = document.getElementById('student-name').value.trim();
  const email = document.getElementById('student-email').value.trim();
  const maxHours = Number(document.getElementById('student-max-hours').value || '');
  if(!name || !email){ alert('Name and email are required'); return; }
  try{
    await fetchJSON(`${API_BASE}/students`, { method:'POST', body: JSON.stringify({ name, email, maxHoursPerDay: isNaN(maxHours) ? null : maxHours }) });
    document.getElementById('student-form').reset();
    await loadStudents();
  }catch(e){}
}

async function handleStudentsTableClick(e){
  if(e.target.matches('button[data-action="delete"]')){
    const id = e.target.dataset.id;
    if(!confirm('Delete student id ' + id + '?')) return;
    try{
      await fetchJSON(`${API_BASE}/students/${id}`, { method:'DELETE' });
      await loadStudents();
    }catch(e){}
  }
}

// ---------- Modules page ----------
async function loadModules(){
  const tbody = document.querySelector('#modules-table tbody');
  tbody.innerHTML = '';
  try{
    const modules = await fetchJSON(`${API_BASE}/modules`);
    modules.forEach(m => {
      const tr = document.createElement('tr');
      tr.innerHTML = `<td>${m.id ?? ''}</td>
                      <td>${escapeHtml(m.name ?? '')}</td>
                      <td>${escapeHtml(m.moduleCode ?? '')}</td>
                      <td>${m.credits ?? ''}</td>
                      <td class="actions">
                        <button class="btn outline" data-action="edit" data-id="${m.id}">Edit</button>
                        <button class="btn outline" data-action="delete" data-id="${m.id}">Delete</button>
                      </td>`;
      tbody.appendChild(tr);
    });
  }catch(e){ console.warn(e); }
}

async function submitModule(e){
  e.preventDefault();
  const id = document.getElementById('module-id').value;
  const name = document.getElementById('module-name').value.trim();
  const code = document.getElementById('module-code').value.trim();
  const credits = Number(document.getElementById('module-credits').value);
  if(!name || !code || isNaN(credits)) { alert('Please complete module fields'); return; }
  try{
    if(id){
      await fetchJSON(`${API_BASE}/modules/${id}`, { method:'PUT', body: JSON.stringify({ name, moduleCode: code, credits }) });
    }else{
      await fetchJSON(`${API_BASE}/modules`, { method:'POST', body: JSON.stringify({ name, moduleCode: code, credits }) });
    }
    document.getElementById('module-form').reset();
    document.getElementById('module-id').value = '';
    await loadModules();
  }catch(e){}
}

async function handleModulesTableClick(e){
  const btn = e.target.closest('button');
  if(!btn) return;
  const action = btn.dataset.action;
  const id = btn.dataset.id;
  if(action === 'delete'){
    if(!confirm('Delete module id ' + id + '?')) return;
    try{ await fetchJSON(`${API_BASE}/modules/${id}`, { method:'DELETE' }); await loadModules(); }catch(e){}
  }else if(action === 'edit'){
    try{
      const module = await fetchJSON(`${API_BASE}/modules/${id}`);
      document.getElementById('module-id').value = module.id || '';
      document.getElementById('module-name').value = module.name || '';
      document.getElementById('module-code').value = module.moduleCode || '';
      document.getElementById('module-credits').value = module.credits ?? '';
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }catch(e){}
  }
}

// ---------- Courseworks page ----------
async function loadCourseworks(){
  const tbody = document.querySelector('#courseworks-table tbody');
  tbody.innerHTML = '';
  try{
    const items = await fetchJSON(`${API_BASE}/courseworks`);
    items.forEach(cw => {
      const tr = document.createElement('tr');
      const d = cw.deadline ? formatDateTime(cw.deadline) : '';
      tr.innerHTML = `<td>${cw.id ?? ''}</td>
                      <td>${escapeHtml(cw.title ?? '')}</td>
                      <td>${escapeHtml(d)}</td>
                      <td>${cw.weighting ?? ''}</td>
                      <td>${cw.estimatedHours ?? ''}</td>
                      <td>${(cw.student && cw.student.id) ? cw.student.id : (cw.studentId ?? '')} ${cw.student && cw.student.name ? ' - ' + escapeHtml(cw.student.name) : ''}</td>
                      <td>${(cw.module && cw.module.id) ? cw.module.id : (cw.moduleId ?? '')} ${cw.module && cw.module.moduleCode ? ' - ' + escapeHtml(cw.module.moduleCode) : ''}</td>
                      <td class="actions">
                        <button class="btn outline" data-action="delete" data-id="${cw.id}">Delete</button>
                      </td>`;
      tbody.appendChild(tr);
    });
  }catch(e){ console.warn(e); }
}

async function submitCoursework(e){
  e.preventDefault();
  const title = document.getElementById('cw-title').value.trim();
  const deadlineInput = document.getElementById('cw-deadline').value;
  const weighting = Number(document.getElementById('cw-weighting').value);
  const hours = Number(document.getElementById('cw-hours').value);
  const studentId = Number(document.getElementById('cw-student').value);
  const moduleId = Number(document.getElementById('cw-module').value);
  if(!title || !deadlineInput || isNaN(weighting) || isNaN(hours) || isNaN(studentId) || isNaN(moduleId)){
    alert('Please complete all coursework fields'); return;
  }
  const deadline = new Date(deadlineInput).toISOString();
  try{
    await fetchJSON(`${API_BASE}/courseworks`, { method:'POST', body: JSON.stringify({ title, deadline, weighting, estimatedHours: hours, studentId, moduleId }) });
    document.getElementById('coursework-form').reset();
    await loadCourseworks();
  }catch(e){}
}

async function handleCourseworksTableClick(e){
  if(e.target.matches('button[data-action="delete"]')){
    const id = e.target.dataset.id;
    if(!confirm('Delete coursework id ' + id + '?')) return;
    try{ await fetchJSON(`${API_BASE}/courseworks/${id}`, { method:'DELETE' }); await loadCourseworks(); }catch(e){}
  }
}

// ---------- Schedule page ----------
async function generateSchedule(){
  if(!confirm('Generate a new schedule? This will call the backend optimisation.')) return;
  try{
    await fetchJSON(`${API_BASE}/schedule/generate`);
    await loadSchedule();
  }catch(e){}
}

function groupByDate(items, keySelector){
  const groups = {};
  items.forEach(it => {
    const key = keySelector(it);
    groups[key] = groups[key] || [];
    groups[key].push(it);
  });
  return groups;
}

function formatDateOnly(value){
  try{ const d = new Date(value); return d.toLocaleDateString(); }catch(e){ return String(value); }
}
function formatDateTime(value){
  try{ const d = new Date(value); return d.toLocaleString(); }catch(e){ return String(value); }
}

async function loadSchedule(){
  const container = document.getElementById('schedule-container');
  container.innerHTML = '';
  try{
    const schedule = await fetchJSON(`${API_BASE}/schedule`);
    if(!Array.isArray(schedule)){
      container.innerHTML = '<p>No schedule data or unexpected format.</p>';
      return;
    }
    const groups = groupByDate(schedule, it => it.date || it.start || it.deadline || JSON.stringify(it));
    Object.keys(groups).sort().forEach(dayKey => {
      const items = groups[dayKey];
      const box = document.createElement('div');
      box.className = 'card schedule-day';
      const dayLabel = (() => {
        try{ const d = new Date(dayKey.includes('T') ? dayKey : (dayKey + 'T00:00:00')); return d.toLocaleDateString(undefined, { weekday:'long', year:'numeric', month:'short', day:'numeric' }); }catch(e){ return dayKey; }
      })();
      const header = document.createElement('div');
      header.className = 'schedule-day-header';
      header.innerHTML = `<h3>${escapeHtml(dayLabel)}</h3>`;

      const table = document.createElement('table');
      table.className = 'schedule-table';
      table.innerHTML = `<thead><tr><th>Time/Task</th><th>Coursework</th><th>Module</th><th>Student</th><th>Hours</th></tr></thead>`;
      const tb = document.createElement('tbody');
      let totalHours = 0;
      items.forEach(it => {
        const tr = document.createElement('tr');
        const task = it.task || '';
        const hours = Number(it.hoursAllocated || it.duration || it.hours || 0);
        totalHours += isNaN(hours) ? 0 : hours;
        const coursework = it.coursework || it.courseworkId || null;
        const cwTitle = coursework && coursework.title ? coursework.title : (it.title || '—');
        const moduleName = coursework && coursework.module && coursework.module.name ? coursework.module.name : (coursework && coursework.moduleName ? coursework.moduleName : '—');
        const studentName = coursework && coursework.student && coursework.student.name ? coursework.student.name : (it.studentName || '—');
        tr.innerHTML = `<td>${escapeHtml(task)}</td>
                        <td>${escapeHtml(cwTitle)}</td>
                        <td>${escapeHtml(moduleName)}</td>
                        <td>${escapeHtml(studentName)}</td>
                        <td>${hours}</td>`;
        tb.appendChild(tr);
      });
      table.appendChild(tb);

      const footer = document.createElement('div');
      footer.className = 'schedule-day-footer';
      footer.innerHTML = `<small class="muted">Total hours: ${totalHours}</small>`;

      box.appendChild(header);
      box.appendChild(table);
      box.appendChild(footer);
      container.appendChild(box);
    });

  }catch(e){ console.warn(e); container.innerHTML = '<p>Error loading schedule</p>'; }
}

// ---------- Utilities & Init ----------
function escapeHtml(s){ return String(s).replace(/[&<>"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c])); }

function init(){
  const page = document.body.dataset.page;
  if(page === 'students'){
    document.getElementById('student-form').addEventListener('submit', addStudent);
    document.querySelector('#students-table tbody').addEventListener('click', handleStudentsTableClick);
    loadStudents();
  }else if(page === 'modules'){
    document.getElementById('module-form').addEventListener('submit', submitModule);
    document.querySelector('#modules-table tbody').addEventListener('click', handleModulesTableClick);
    loadModules();
  }else if(page === 'courseworks'){
    document.getElementById('coursework-form').addEventListener('submit', submitCoursework);
    document.querySelector('#courseworks-table tbody').addEventListener('click', handleCourseworksTableClick);
    loadCourseworks();
  }else if(page === 'schedule'){
    document.getElementById('generate-schedule').addEventListener('click', generateSchedule);
    document.getElementById('refresh-schedule').addEventListener('click', loadSchedule);
    loadSchedule();
  }
}

if(document.readyState === 'loading') document.addEventListener('DOMContentLoaded', init); else init();