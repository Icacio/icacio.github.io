<script setup>
import { computed } from "vue";
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
	audioTag: {
		type:Object,
		required:true,
	},
	currentTime: {
		type: Number,
		required: true
	},
	shuffle: {
		type: Function,
		required: true
	}
})

const minutes = computed(()=>{
	return secondsToMinutes(props.currentTime);
})
const duration = computed(()=>{
	return secondsToMinutes(props.time);
})

const emit = defineEmits();
function secondsToMinutes(seconds) {
	if (isNaN(seconds)) return "00:00:00"
		let hours = Math.floor(seconds/3600);
		seconds -= hours*3600;
		let minutes = Math.floor(seconds/60);
		seconds -= minutes*60;
		return hours+":"+minutes+":"+Math.floor(seconds);
}

function play() {
	let audio = props.audioTag;
	if (audio && !props.modelValue) {
		audio.play();
		emit("update:modelValue", true);
	}
}

function pause() {
	let audio = props.audioTag;
	if (audio && props.modelValue) {
		audio.pause();
		emit("update:modelValue", false); // update isPlaying in parent
	}
};
</script>

<template>
	<p>{{ minutes }}/{{ duration }}</p>
	<input type="range" id="a" name="a" v-model="audioTag.currentTime" min=0 :max="time">
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
	<button @click="shuffle()">
		<img src="/images/shuffle.png">
	</button>
	<div>
		<input type="range" min="0" max="1" step="0.01" v-model="audioTag.volume">
	</div>
</template>

<style scoped>
input[type=range] {
	width: 100%;
}
button {
    background-color:transparent;
	border: none;
	border-radius: 50%;
}
button:hover {
	background-color: #00000010;
	border-radius: 50%;
}
</style>
