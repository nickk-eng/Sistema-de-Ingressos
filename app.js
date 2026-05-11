const STORAGE_KEY = "financeiro-3d-data";

const initialState = {
  filaments: [],
  records: [],
  settings: {
    printerKw: 0.12,
    energyKwh: 0.95,
    wearHour: 1.5
  }
};

let state = loadState();

const formatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL"
});

const els = {
  tabs: document.querySelectorAll(".nav-tab"),
  panels: document.querySelectorAll(".tab-panel"),
  filamentForm: document.querySelector("#filamentForm"),
  filamentName: document.querySelector("#filamentName"),
  filamentType: document.querySelector("#filamentType"),
  filamentKg: document.querySelector("#filamentKg"),
  filamentPrice: document.querySelector("#filamentPrice"),
  filamentTable: document.querySelector("#filamentTable"),
  recordForm: document.querySelector("#recordForm"),
  recordType: document.querySelector("#recordType"),
  productName: document.querySelector("#productName"),
  recordFilament: document.querySelector("#recordFilament"),
  recordGrams: document.querySelector("#recordGrams"),
  salePrice: document.querySelector("#salePrice"),
  clientName: document.querySelector("#clientName"),
  clientContact: document.querySelector("#clientContact"),
  printHours: document.querySelector("#printHours"),
  saleFields: document.querySelector(".sale-fields"),
  recordEstimate: document.querySelector("#recordEstimate"),
  estimateDetails: document.querySelector("#estimateDetails"),
  recordsTable: document.querySelector("#recordsTable"),
  stockSummary: document.querySelector("#stockSummary"),
  recentRecords: document.querySelector("#recentRecords"),
  revenueMetric: document.querySelector("#revenueMetric"),
  costMetric: document.querySelector("#costMetric"),
  profitMetric: document.querySelector("#profitMetric"),
  gramsMetric: document.querySelector("#gramsMetric"),
  settingsForm: document.querySelector("#settingsForm"),
  printerKw: document.querySelector("#printerKw"),
  energyKwh: document.querySelector("#energyKwh"),
  wearHour: document.querySelector("#wearHour"),
  exportDataBtn: document.querySelector("#exportDataBtn")
};

function loadState() {
  const saved = localStorage.getItem(STORAGE_KEY);
  if (!saved) return structuredClone(initialState);

  try {
    const parsed = JSON.parse(saved);
    return {
      ...structuredClone(initialState),
      ...parsed,
      settings: {
        ...initialState.settings,
        ...(parsed.settings || {})
      }
    };
  } catch {
    return structuredClone(initialState);
  }
}

function saveState() {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
}

function money(value) {
  return formatter.format(Number(value) || 0);
}

function safeText(value) {
  const element = document.createElement("span");
  element.textContent = value || "";
  return element.innerHTML;
}

function grams(value) {
  return `${(Number(value) || 0).toLocaleString("pt-BR", { maximumFractionDigits: 1 })} g`;
}

function id() {
  return window.crypto && crypto.randomUUID ? crypto.randomUUID() : `${Date.now()}-${Math.random()}`;
}

function filamentCostPerGram(filament) {
  return filament.price / filament.initialGrams;
}

function usedByFilament(filamentId) {
  return state.records
    .filter((record) => record.filamentId === filamentId)
    .reduce((total, record) => total + record.grams, 0);
}

function availableGrams(filament) {
  return Math.max(0, filament.initialGrams - usedByFilament(filament.id));
}

function getSelectedFilament() {
  return state.filaments.find((filament) => filament.id === els.recordFilament.value);
}

function calculateRecordCosts(filament, gramsUsed, hours, type) {
  const materialCost = filament ? filamentCostPerGram(filament) * gramsUsed : 0;
  if (type === "personal") {
    return {
      materialCost,
      energyCost: 0,
      wearCost: 0,
      totalCost: 0
    };
  }

  const energyCost = state.settings.printerKw * state.settings.energyKwh * hours;
  const wearCost = state.settings.wearHour * hours;
  return {
    materialCost,
    energyCost,
    wearCost,
    totalCost: materialCost + energyCost + wearCost
  };
}

function switchTab(tabId) {
  els.tabs.forEach((tab) => tab.classList.toggle("active", tab.dataset.tab === tabId));
  els.panels.forEach((panel) => panel.classList.toggle("active", panel.id === tabId));
}

function renderFilamentOptions() {
  if (!state.filaments.length) {
    els.recordFilament.innerHTML = '<option value="">Cadastre um filamento primeiro</option>';
    return;
  }

  els.recordFilament.innerHTML = state.filaments
    .map((filament) => `<option value="${filament.id}">${safeText(filament.name)} - ${grams(availableGrams(filament))}</option>`)
    .join("");
}

function renderFilaments() {
  if (!state.filaments.length) {
    els.filamentTable.innerHTML = '<tr><td colspan="6" class="empty-state">Nenhum filamento cadastrado ainda.</td></tr>';
    els.stockSummary.innerHTML = '<tr><td colspan="4" class="empty-state">Cadastre seus filamentos para acompanhar o estoque.</td></tr>';
    return;
  }

  els.filamentTable.innerHTML = state.filaments.map((filament) => {
    return `
      <tr>
        <td><strong>${safeText(filament.name)}</strong></td>
        <td>${safeText(filament.type) || "-"}</td>
        <td>${grams(filament.initialGrams)}</td>
        <td>${grams(availableGrams(filament))}</td>
        <td>${money(filamentCostPerGram(filament))}</td>
        <td><button class="danger-button" data-delete-filament="${filament.id}" type="button">Excluir</button></td>
      </tr>
    `;
  }).join("");

  els.stockSummary.innerHTML = state.filaments.map((filament) => {
    return `
      <tr>
        <td><strong>${safeText(filament.name)}</strong><br><span class="muted">${safeText(filament.type) || "Sem tipo"}</span></td>
        <td>${grams(availableGrams(filament))}</td>
        <td>${grams(usedByFilament(filament.id))}</td>
        <td>${money(filamentCostPerGram(filament))}</td>
      </tr>
    `;
  }).join("");
}

function renderRecords() {
  if (!state.records.length) {
    els.recordsTable.innerHTML = '<tr><td colspan="8" class="empty-state">Nenhum produto registrado ainda.</td></tr>';
    els.recentRecords.innerHTML = '<p class="empty-state">Os ultimos registros vao aparecer aqui.</p>';
    return;
  }

  const rows = [...state.records].reverse().map((record) => {
    const profit = record.type === "sale" ? record.salePrice - record.totalCost : 0;
    const costLabel = record.type === "sale" ? money(record.totalCost) : "Baixa de estoque";
    return `
      <tr>
        <td>${new Date(record.createdAt).toLocaleDateString("pt-BR")}</td>
        <td><strong>${safeText(record.productName)}</strong><br><span class="muted">${record.grams} g de ${safeText(record.filamentName)}</span></td>
        <td><span class="status-pill ${record.type === "personal" ? "personal" : ""}">${record.type === "sale" ? "Venda" : "Pessoal"}</span></td>
        <td>${record.type === "sale" ? `${safeText(record.clientName) || "-"}<br><span class="muted">${safeText(record.clientContact)}</span>` : "-"}</td>
        <td>${costLabel}<br><span class="muted">Mat. ${money(record.materialCost)}</span></td>
        <td>${record.type === "sale" ? money(record.salePrice) : "-"}</td>
        <td>${money(profit)}</td>
        <td><button class="danger-button" data-delete-record="${record.id}" type="button">Excluir</button></td>
      </tr>
    `;
  }).join("");

  els.recordsTable.innerHTML = rows;

  els.recentRecords.innerHTML = [...state.records].reverse().slice(0, 5).map((record) => {
    return `
      <div class="activity-item">
        <strong>${safeText(record.productName)}</strong>
        <span>${record.type === "sale" ? "Venda" : "Uso pessoal"} - ${grams(record.grams)} - ${record.type === "sale" ? money(record.totalCost) : "estoque baixado"}</span>
      </div>
    `;
  }).join("");
}

function renderDashboard() {
  const totals = state.records.reduce((acc, record) => {
    acc.revenue += record.type === "sale" ? record.salePrice : 0;
    acc.cost += record.type === "sale" ? record.totalCost : 0;
    acc.grams += record.grams;
    return acc;
  }, { revenue: 0, cost: 0, grams: 0 });

  els.revenueMetric.textContent = money(totals.revenue);
  els.costMetric.textContent = money(totals.cost);
  els.profitMetric.textContent = money(totals.revenue - totals.cost);
  els.gramsMetric.textContent = grams(totals.grams);
}

function renderSettings() {
  els.printerKw.value = state.settings.printerKw;
  els.energyKwh.value = state.settings.energyKwh;
  els.wearHour.value = state.settings.wearHour;
}

function renderEstimate() {
  const filament = getSelectedFilament();
  const type = els.recordType.value;
  const gramsUsed = Number(els.recordGrams.value) || 0;
  const hours = type === "sale" ? Number(els.printHours.value) || 0 : 0;
  const costs = calculateRecordCosts(filament, gramsUsed, hours, type);

  els.saleFields.classList.toggle("hidden", type === "personal");

  if (!filament) {
    els.recordEstimate.textContent = type === "personal" ? grams(gramsUsed) : money(costs.totalCost);
    els.estimateDetails.textContent = "Escolha um filamento para calcular.";
    return;
  }

  if (type === "personal") {
    els.recordEstimate.textContent = grams(gramsUsed);
    els.estimateDetails.textContent = `Custo de filamento: ${money(costs.materialCost)}. O estoque sera baixado em ${grams(gramsUsed)}.`;
    return;
  }

  els.recordEstimate.textContent = money(costs.totalCost);
  els.estimateDetails.textContent = `Filamento: ${money(costs.materialCost)} | Energia: ${money(costs.energyCost)} | Desgaste: ${money(costs.wearCost)}`;
}

function renderAll() {
  renderFilamentOptions();
  renderFilaments();
  renderRecords();
  renderDashboard();
  renderSettings();
  renderEstimate();
}

function handleFilamentSubmit(event) {
  event.preventDefault();

  const kg = Number(els.filamentKg.value);
  const price = Number(els.filamentPrice.value);
  state.filaments.push({
    id: id(),
    name: els.filamentName.value.trim(),
    type: els.filamentType.value.trim(),
    initialGrams: kg * 1000,
    price,
    createdAt: new Date().toISOString()
  });

  saveState();
  els.filamentForm.reset();
  els.filamentKg.value = 1;
  renderAll();
}

function handleRecordSubmit(event) {
  event.preventDefault();

  const filament = getSelectedFilament();
  if (!filament) {
    alert("Cadastre e escolha um filamento antes de registrar o produto.");
    return;
  }

  const gramsUsed = Number(els.recordGrams.value);
  if (gramsUsed > availableGrams(filament)) {
    alert(`Estoque insuficiente. Disponivel: ${grams(availableGrams(filament))}.`);
    return;
  }

  const type = els.recordType.value;
  const hours = type === "sale" ? Number(els.printHours.value) || 0 : 0;
  const costs = calculateRecordCosts(filament, gramsUsed, hours, type);

  state.records.push({
    id: id(),
    type,
    productName: els.productName.value.trim(),
    filamentId: filament.id,
    filamentName: filament.name,
    grams: gramsUsed,
    salePrice: type === "sale" ? Number(els.salePrice.value) || 0 : 0,
    clientName: type === "sale" ? els.clientName.value.trim() : "",
    clientContact: type === "sale" ? els.clientContact.value.trim() : "",
    hours,
    ...costs,
    createdAt: new Date().toISOString()
  });

  saveState();
  els.recordForm.reset();
  renderAll();
  switchTab("dashboard");
}

function handleSettingsSubmit(event) {
  event.preventDefault();
  state.settings = {
    printerKw: Number(els.printerKw.value) || 0,
    energyKwh: Number(els.energyKwh.value) || 0,
    wearHour: Number(els.wearHour.value) || 0
  };
  saveState();
  renderAll();
}

function deleteFilament(filamentId) {
  const hasRecords = state.records.some((record) => record.filamentId === filamentId);
  if (hasRecords) {
    alert("Esse filamento possui registros. Exclua os produtos primeiro.");
    return;
  }

  state.filaments = state.filaments.filter((filament) => filament.id !== filamentId);
  saveState();
  renderAll();
}

function deleteRecord(recordId) {
  state.records = state.records.filter((record) => record.id !== recordId);
  saveState();
  renderAll();
}

function exportData() {
  const payload = JSON.stringify(state, null, 2);
  const blob = new Blob([payload], { type: "application/json" });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = "financeiro-3d-dados.json";
  link.click();
  URL.revokeObjectURL(url);
}

els.tabs.forEach((tab) => {
  tab.addEventListener("click", () => switchTab(tab.dataset.tab));
});

els.filamentForm.addEventListener("submit", handleFilamentSubmit);
els.recordForm.addEventListener("submit", handleRecordSubmit);
els.settingsForm.addEventListener("submit", handleSettingsSubmit);
els.exportDataBtn.addEventListener("click", exportData);

["input", "change"].forEach((eventName) => {
  els.recordForm.addEventListener(eventName, renderEstimate);
});

document.addEventListener("click", (event) => {
  const filamentId = event.target.dataset.deleteFilament;
  const recordId = event.target.dataset.deleteRecord;
  if (filamentId) deleteFilament(filamentId);
  if (recordId) deleteRecord(recordId);
});

renderAll();
