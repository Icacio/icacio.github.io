<script setup>
import MediaController from './components/MediaController.vue'
import {ref, onMounted, useTemplateRef} from "vue";

const song = ref(null);
let songs = null;

let index = 0;
const isPlaying = ref(false);
const duration = ref(1);
const audioTag = useTemplateRef("audioElement")
const currentTime = ref(0)
const folderName = ref("http://localhost:8000/");


function loadMusic (files) {
		songs = files;
		song.value = files[0].link;
		let audio = document.getElementById("audio");
		if (audio) {
			duration.value = audio.duration;
		}
}
function loadLocal() {
	try {
		fetch(folderName.value+".json")
			.then(response => {
				const contentType = response.headers.get("content-type");
				if (contentType && contentType.includes("application/json")) {
					return response.json();
				} else {
					throw new Error("Invalid JSON file");
				}}).then(data => {
					if (data && Array.isArray(data)) {
						loadMusic(data);
					} else {
						console,error("Invalid JSON format");
					}
				})
			.catch(error => {
				console.error("Error loading songs:", error);
			});
	}
	catch (e) {
		console.error(e);
	}
}

const updateTime = () => {
	if(audioTag.value) {
		currentTime.value = audioTag.value.currentTime;
	}
}
function setDuration() {
	if (audioTag.value) {
		duration.value = audioTag.value.duration;
	}
}
function setSong(address,i) {
	let audio = audioTag.value;
	audio.pause();
	song.value = address;
	audio.load();
	audio.play();
	index = i;
	setDuration();
}

function songPaused() {
	isPlaying.value = false;
}

function songPlayed() {
	isPlaying.value = true;
}
function songNext() {
	let audio = audioTag.value;
	audio.pause();
	song.value = songs[++index].link;
	audio.load();
	audio.play();
	setDuration();
}

function songPrevious() {
	let audio = audioTag.value;
	audio.pause();
	song.value = songs[--index].link;
	audio.load();
	audio.play();
	setDuration();
}
function shuffle() {
	const current = songs[index];
	songs.splice(index, 1);
	songs.sort(() => Math.random() - 0.5);
	songs.unshift(current);
	index = 0;
}
</script>

<template>
	<div v-if="songs" class="left">
		<p v-for="(songListing,i) in songs">
			<label v-if="songListing.link === song">
				{{ songListing.name }}
			</label>
			<button v-else @click="setSong(songListing.link,i)">
				{{ songListing.name }}
			</button>	
		</p>
	</div>
	<h1>IctoPlayer</h1>
	<p>
		<template v-if="song">
			<audio id="audio" preload="metadata"
				@play="songPlayed"
				@pause="songPaused"
				@ended="songNext"
				@timeupdate="updateTime"
				@loadedmetadata="setDuration"
				ref="audioElement">
					<source :src="song" type="audio/mpeg">
			</audio>
			{{ songs[index].name }} <br>
			<MediaController v-if="audioTag" v-model="isPlaying"
				:nextFunction="songNext"
				:previousFunction="songPrevious"
				:time=duration
				:currentTime="currentTime"
				:audioTag="audioTag" 
				:shuffle="shuffle"/>
		</template>
		<div v-else>
			<form @submit.prevent="loadLocal">
				<input type="text" v-model="folderName">
				<button type="submit">Load</button>
			</form>
		</div>
	</p>
</template>