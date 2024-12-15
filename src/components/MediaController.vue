<script setup>
import {ref, defineEmits } from "vue";
const props = defineProps({
	modelValue: Boolean
})
const emit = defineEmits();
const timer = ref(0);

function play() {
	if (audio && !props.modelValue) {
		let audio = document.getElementById("audio");
		audio.play();
		emit("update:modelValue", true);
	}
}

function pause() {
	let audio = document.getElementById("audio");
	if (audio && props.modelValue) {
		audio.pause();
		emit("update:modelValue", false); // update isPlaying in parent
	}
};
</script>

<template>
	<input type="range" id="a" name="a" v-bind:value="timer">
 	<button>
		<img src="/images/previous.png">
	</button>
	<button v-show="!modelValue" @click="play()">
		<img src="/images/play.png">
	</button>
	<button v-show="modelValue" @click="pause()">
		<img src="/images/pause.png">
	</button>
	<button>
		<img src="/images/previous.png"
		style="transform: scaleX(-1);">
	</button>
</template>

<style scoped>
input[type=range] {
	width: 100%;
}


</style>
