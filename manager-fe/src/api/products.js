const API_BASE = import.meta.env.VITE_API_URL || ''

async function safeJson(res){
    if (res.status === 204) return null
    try {
        return await res.json()
    } catch {
        return null
    }
}

export async function listProducts() {
    const res = await fetch(`${API_BASE}/api/products`)
    if (!res.ok && res.status !== 204)
        throw new Error(`GET /api/products status ${res.status}`)
    return (res.status === 204) ? [] : (await res.json())
}


export async function createProduct(product) {
    const res = await fetch(`${API_BASE}/api/products`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product),
    })
    if (res.status !== 201) throw new Error(`POST /api/products ${res.status}`)
    return safeJson(res)
}

export async function getProductById(id) {
    const res = await fetch(`${API_BASE}/api/products/${encodeURIComponent(id)}`)
    if (!res.ok) throw new Error(`GET /api/products/${id} status ${res.status}`)
    return res.json()
}

export async function updateProductById(id, product) {
    const res = await fetch(`${API_BASE}/api/products/${encodeURIComponent(id)}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(product),
    })
    if (!res.ok) throw new Error(`PUT /api/products/${id} status ${res.status}`)
    return safeJson(res) // se restituisci 200 con body, lo prende; se 200 senza body, torna null
}

export async function deleteProductById(id) {
    const res = await fetch(`${API_BASE}/api/products/${encodeURIComponent(id)}`, {
        method: 'DELETE',
    })
    if (res.status !== 204 && !res.ok) throw new Error(`DELETE /api/products/${id} status ${res.status}`)
}
