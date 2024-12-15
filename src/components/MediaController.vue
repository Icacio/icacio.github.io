<script setup>
import {ref} from "vue";
const props = defineProps({
	time: Number,
})

let intervalId;
let timer = ref(0);
let isPlaying = ref(false);

function play() {
var audio = document.getElementById("audio");
	audio.play();
	isPlaying.value = true;
	intervalId = setInterval(() =>{
		timer.value += props.time/100;
	},1000);
}

function pause() {
  clearInterval(intervalId);
  isPlaying.value = false;
  var audio = document.getElementById("audio");
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
