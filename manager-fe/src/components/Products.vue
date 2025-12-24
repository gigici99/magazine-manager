
<template>
  <div>
    <h1 data-test="title">Gestione Prodotti</h1>

    <button data-test="add-product" @click="openCreate">Aggiungi prodotto</button>

    <form v-if="showForm" @submit.prevent="submitForm">
      <input
          data-test="product-name"
          v-model="form.name"
          placeholder="Nome"
          required
      />
      <input
          data-test="product-code"
          v-model="form.codeProduct"
          placeholder="Codice Prodotto"
          required
      />
      <input
          data-test="product-quantity"
          v-model="form.quantity"
          placeholder="Quantità"
          type="number"
          step="1"
          min="0"
          required
      />
      <button data-test="save-product" type="submit">
        {{ formMode === 'create' ? 'Salva' : 'Aggiorna' }}
      </button>
      <button data-test="cancel-product" type="button" @click="cancel">Annulla</button>
    </form>

    <div v-if="!products.length" data-test="empty" style="margin-top:1rem;color:#555">
      Nessun prodotto presente.
    </div>

    <table v-else data-test="products-table">
      <thead>
      <tr><th>Nome</th><th>Quantità</th><th>Codice Prodotto</th><th>Azioni</th></tr>
      </thead>
      <tbody>
      <tr v-for="p in products" :key="p.id ?? p.codeProduct ?? p.name">
        <td>{{ p.name }}</td>
        <td>{{ p.quantity }}</td>
        <td>{{ p.codeProduct }}</td>
        <td>
          <button data-test="edit-product" @click="openEdit(p)">Modifica</button>
          <button data-test="delete-product" @click="deleteP(p.id)">Elimina</button>
        </td>
      </tr>
      </tbody>
    </table>

    <p v-if="error" data-test="error" style="color:red">{{ error }}</p>

    <!-- Debug opzionale: rimuovi in prod
    <pre style="margin-top: 8px">{{ products }}</pre>
    -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listProducts, createProduct, updateProductById, deleteProductById } from '../api/products'

const products = ref([])
const showForm = ref(false)
const formMode = ref('create')
const form = ref({ id: null, name: '', codeProduct: '', quantity: '' })
const error = ref('')

// Normalizza i campi in modo difensivo (se il backend usa nomi diversi)
function normalizeProduct(p) {
  return {
    id: p.id ?? p.productId ?? p._id ?? null,
    name: p.name ?? p.nameProduct ?? p.productName ?? p.nome ?? '',
    quantity: p.quantity ?? p.qty ?? p.quantita ?? 0,
    codeProduct: p.codeProduct ?? p.code ?? p.codice ?? ''
  }
}

async function load() {
  error.value = ''
  try {
    const data = await listProducts()
    products.value = (data || []).map(normalizeProduct)
  } catch (e) {
    error.value = e.message
    console.error('load error', e)
  }
}

function openCreate() {
  formMode.value = 'create'
  form.value = { id: null, name: '', codeProduct: '', quantity: '' }
  showForm.value = true
}

function openEdit(p) {
  formMode.value = 'edit'
  const n = normalizeProduct(p)
  form.value = {
    id: n.id,
    name: n.name,
    codeProduct: n.codeProduct,
    quantity: String(n.quantity ?? '')
  }
  showForm.value = true
}

async function submitForm() {
  error.value = ''
  const rawName = form.value?.name ?? ''
  const rawQty = form.value?.quantity ?? ''
  const rawCode = form.value?.codeProduct ?? ''

  const payload = {
    name: String(rawName).trim(),               // <-- TRIM QUI
    quantity: parseInt(String(rawQty), 10),
    codeProduct: String(rawCode).trim()
  }

  try {
    if (!payload.name) throw new Error('Nome obbligatorio')
    if (!payload.codeProduct) throw new Error('Codice prodotto obbligatorio')
    if (Number.isNaN(payload.quantity) || payload.quantity < 0) {
      throw new Error('Quantità non valida')
    }

    if (formMode.value === 'create') {
      await createProduct(payload)
    } else {
      if (!form.value?.id) throw new Error('ID mancante per aggiornamento')
      await updateProductById(form.value.id, payload)
    }

    showForm.value = false
    await load()
  } catch (e) {
    error.value = e.message
    console.error('submitForm error', e, { form: form.value, payload })
  }
}

async function deleteP(id) {
  error.value = ''
  try {
    await deleteProductById(id)
    await load()
  } catch (e) {
    error.value = e.message
    console.error('delete error', e)
  }
}

function cancel() {
  // opzionale: resetta i campi
  form.value = { id: null, name: '', codeProduct: '', quantity: '' }
  showForm.value = false
}

onMounted(load)
</script>

<style scoped>
table { margin-top: 1rem; border-collapse: collapse; width: 100%; }
th, td { border: 1px solid #ddd; padding: 0.5rem; }
button { margin-right: .5rem; }
form { margin-top: 1rem; display: grid; grid-template-columns: 1fr 1fr; gap: .5rem; max-width: 520px; }
</style>
