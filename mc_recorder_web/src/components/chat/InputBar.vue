<template>
  <div class="flex items-end gap-2 rounded-2xl border border-[#1F242C] bg-[#161B22] p-2 transition-all duration-200 focus-within:border-amber-400/40 focus-within:bg-[#1F242C]">
    <textarea
      ref="textareaRef"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      @keydown.enter.exact.prevent="handleEnter"
      @keydown.enter.shift.exact="() => {}"
      :disabled="loading"
      placeholder="问点什么... (Enter 发送, Shift+Enter 换行)"
      rows="1"
      class="flex-1 resize-none bg-transparent px-2 py-1.5 text-[14px] text-[#E6E6E8] placeholder-[#484F58] outline-none disabled:opacity-50"
      style="max-height: 180px"
    ></textarea>

    <button
      @click="$emit('send')"
      :disabled="loading || !modelValue.trim()"
      class="group flex h-9 w-9 flex-shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-amber-400 to-yellow-500 text-[#0D1117] shadow-lg shadow-amber-500/20 transition-all duration-200 hover:brightness-110 hover:shadow-amber-500/40 active:scale-95 disabled:cursor-not-allowed disabled:bg-[#1F242C] disabled:from-[#1F242C] disabled:to-[#1F242C] disabled:text-[#484F58] disabled:shadow-none"
      title="发送 (Enter)"
    >
      <svg v-if="!loading" class="h-4 w-4 transition-transform duration-200 group-hover:translate-x-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5">
        <path stroke-linecap="round" stroke-linejoin="round" d="M5 12h14M13 5l7 7-7 7" />
      </svg>
      <svg v-else class="h-4 w-4 animate-spin" fill="none" viewBox="0 0 24 24">
        <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3" stroke-opacity="0.25"></circle>
        <path fill="currentColor" d="M4 12a8 8 0 018-8v3a5 5 0 00-5 5H4z"></path>
      </svg>
    </button>
  </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'

const props = defineProps({
  modelValue: { type: String, default: '' },
  loading: { type: Boolean, default: false },
})
const emit = defineEmits(['update:modelValue', 'send'])

const textareaRef = ref(null)

function handleEnter() {
  if (props.modelValue.trim() && !props.loading) emit('send')
}

watch(() => props.modelValue, async () => {
  await nextTick()
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
    textareaRef.value.style.height = Math.min(textareaRef.value.scrollHeight, 180) + 'px'
  }
})
</script>
