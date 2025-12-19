let tickIntervalId = null;
const eventCounts = {};

async function api(path) {
    const res = await fetch('/simulation/' + path);
    const text = await res.text();
    try {
        return JSON.parse(text);
    } catch {
        return text;
    }
}

async function runTick() {
    const out = await api('tick?n=1');
    const tickOut = document.getElementById('tickOut');
    tickOut.value += out + '\n';
    tickOut.scrollTop = tickOut.scrollHeight;
}

function startSimulation() {
    const interval = parseInt(document.getElementById('tickInterval').value) || 1000;
    if (!tickIntervalId) {
        tickIntervalId = setInterval(runTick, interval);
    }
}

function stopSimulation() {
    if (tickIntervalId) {
        clearInterval(tickIntervalId);
        tickIntervalId = null;
    }
}

function getSelectedReportType() {
    return document.querySelector('input[name="reportType"]:checked').value;
}

function formatConfiguration(config) {
    let text = '--- Factory Configuration ---\n';

    config.productionLines.forEach(line => {
        text += `=== Production Line: ${line.name} ===\n`;
        line.stations.forEach(station => {
            text += `${station.type} "${station.name}" | Input: ${station.input} | Output: ${station.output}\n`;
        });
        text += '\n';
    });

    return text;
}

function formatConsumption(consumption) {
    let text = '--- Consumption ---\n';

    consumption.lineConsumptions.forEach(lc => {
        const profit = lc.revenue - lc.wages - lc.materialCost;

        text += `${lc.lineName}:\n`;
        text += `  electricity = ${lc.electricity.toFixed(1)} kWh\n`;
        text += `  oil         = ${lc.oil.toFixed(1)} l\n`;
        text += `  wages       = ${lc.wages.toFixed(1)} $\n`;
        text += `  materials   = ${lc.materialCost.toFixed(1)} $\n`;
        text += `  revenue     = ${lc.revenue.toFixed(1)} $\n`;
        text += `  PROFIT      = ${profit.toFixed(1)} $\n\n`;
    });

    text += `TOTALS:\n`;
    text += `  electricity = ${consumption.totalElectricity.toFixed(1)} kWh\n`;
    text += `  oil         = ${consumption.totalOil.toFixed(1)} l\n`;
    text += `  wages       = ${consumption.totalWages.toFixed(1)} $\n`;
    text += `  materials   = ${consumption.totalMaterialCosts.toFixed(1)} $\n`;
    text += `  revenue     = ${consumption.totalRevenue.toFixed(1)} $\n`;
    text += `  PROFIT      = ${(consumption.totalRevenue - consumption.totalWages - consumption.totalMaterialCosts).toFixed(1)} $\n\n`;

    return text;
}

function formatOutages(outages) {
    return (
        `--- Outages ---\n` +
        `Longest: ${outages.longestOutage.toFixed(2)} ticks\n` +
        `Shortest: ${outages.shortestOutage.toFixed(2)} ticks\n` +
        `Average: ${outages.averageOutage.toFixed(2)} ticks\n` +
        `Avg repair wait: ${outages.averageRepairWait.toFixed(2)} ticks\n`
    );
}

function formatEvents(events) {
    let text = '--- Event Summary ---\n';

    Object.entries(events.counts).forEach(([type, count]) => {
        text += `${type}: ${count}\n`;
    });

    return text + '\n';
}

async function loadState() {
    const report = await api('state');
    const outEl = document.getElementById('stateOut');

    if (!report) {
        outEl.value = 'No data';
        return;
    }

    const type = getSelectedReportType();
    let text = '';

    if (type === 'all' || type === 'config') {
        text += formatConfiguration(report.configuration);
    }

    if ((type === 'all' || type === 'events') && report.events) {
        text += formatEvents(report.events);
    }

    if ((type === 'all' || type === 'consumption') && report.consumption) {
        text += formatConsumption(report.consumption);
    }

    if ((type === 'all' || type === 'outages') && report.outages) {
        text += formatOutages(report.outages);
    }

    outEl.value = text;
    outEl.scrollTop = outEl.scrollHeight;
}

// SSE
const sse = new EventSource("/simulation/events/stream");
sse.onmessage = function (e) {
    const ev = JSON.parse(e.data);
    eventCounts[ev.type] = (eventCounts[ev.type] || 0) + 1;

    const log = document.getElementById("log");
    const line = document.createElement("div");
    line.textContent = ev.message;
    log.appendChild(line);
    log.scrollTop = log.scrollHeight;
};

// Event bindings
document.getElementById('startBtn').addEventListener('click', startSimulation);
document.getElementById('stopBtn').addEventListener('click', stopSimulation);
document.getElementById('loadStateBtn').addEventListener('click', loadState);
