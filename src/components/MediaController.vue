<script setup>
import {ref} from "vue";
const props = defineProps({
	time: Number
})
const timer = ref(0);
const isPlaying = ref(false);

function play() {
	if (!isPlaying.value) {
		var audio = document.getElementById("audio");
		audio.play();
		isPlaying.value = true;
		console.log("ture");
	}
}

function pause() {
	isPlaying.value = false;;
	audio.pause();
};
</script>

<template>
	<input type="range" id="a" name="a" v-bind:value="timer">
 	<button>
		<img src="/images/previous.png">
	</button>
	<button v-show="!isPlaying" @click="play()">
		<img src="/images/play.png">
	</button>
	<button v-show="isPlaying" @click="pause()">
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
