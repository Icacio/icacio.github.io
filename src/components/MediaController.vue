<script setup>
import {ref } from "vue";
const props = defineProps({
	modelValue: Boolean,
	nextFunction: {
		type: Function,
		required: true
	},
	previousFunction: {
		type: Function,
		required: true
	},
	time: Number,
})
const emit = defineEmits();
const timer = ref(0);
let intervalId;
function minutes() {
	let audio = document.getElementById("audio");
	if (audio) {
		let seconds = audio.currentTime;
		let hours = Math.floor(seconds/60);
		seconds -= hours*60;
		let minutes = Math.floor(seconds/60);
		seconds -= minutes*60;
		return hours+":"+minutes+":"+Math.floor(seconds);
	}
	return "00:00:00"
}

function play() {
	let audio = document.getElementById("audio");
	if (audio && !props.modelValue) {
		audio.play();
		emit("update:modelValue", true);
	}
	intervalId = setInterval(() =>{
		timer.value += 100 / props.time;
	},1000);
}

function pause() {
	clearInterval(intervalId);
	let audio = document.getElementById("audio");
	if (audio && props.modelValue) {
		audio.pause();
		emit("update:modelValue", false); // update isPlaying in parent
	}
};
</script>

<template>
	<p>{{ minutes() }}</p>
	<input type="range" id="a" name="a" v-bind:value="timer">
 	<button @click="previousFunction()">
		<img src="/images/previous.png">
	</button>
	<button v-show="!modelValue" @click="play()">
		<img src="/images/play.png">
	</button>
	<button v-show="modelValue" @click="pause()">
		<img src="/images/pause.png">
	</button>
	<button @click="nextFunction()">
		<img src="/images/previous.png"
		style="transform: scaleX(-1);">
	</button>
</template>

<style scoped>
input[type=range] {
	width: 100%;
}


</style>
