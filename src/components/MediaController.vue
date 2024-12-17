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
	}
})

const emit = defineEmits();

const minutes = computed(()=>{
	return secondsToMinutes(props.currentTime);
})
const duration = computed(()=>{
	return secondsToMinutes(props.time);
})
function secondsToMinutes(seconds) {
	if (isNaN(seconds)) return "00:00:00"
	let minutes = Math.floor(seconds/60);
	seconds -= minutes*60;
	let hours = Math.floor(seconds/60);
	seconds -= hours*60;
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
</template>

<style scoped>
input[type=range] {
	width: 100%;
}


</style>
