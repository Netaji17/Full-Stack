const box = document.getElementById("box");

let x = 100;
let y = 100;
const step = 10;

document.addEventListener("keydown", (event) => {
    switch (event.key) {
        case "ArrowUp":
            y -= step;
            break;
        case "ArrowDown":
            y += step;
            break;
        case "ArrowLeft":
            x -= step;
            break;
        case "ArrowRight":
            x += step;
            break;
    }

    box.style.left = x + "px";
    box.style.top = y + "px";
});
