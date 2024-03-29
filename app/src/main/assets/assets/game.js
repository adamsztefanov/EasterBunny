function testQuery() {
  setInterval(function() {
    console.log("testQuery");
    $.ajax({
      url: "http://localhost:50000",
      // The name of the callback parameter, as specified by the YQL service
      jsonp: "callback",
      // Tell jQuery we're expecting JSONP
      dataType: "jsonp",
      // Tell YQL what we want and that we want JSON
      data: {
        "alma": "körte",
        "banan": "alma"
      },
      // Work with the response
      success: function (r) {
        console.log("response")
        console.log(r);
      },
      getJSON: function(r) {
         console.log("response")
         console.log(r);
      }
    });
  }, 5000);
}

// TODO! fix bug on keypress left right
//
// CONSTANTS
//
const GAME_TIME = 120;
const original_width = 422;
const original_height = 522;
const border_pixels = 6;

// reinitables
let time_left = GAME_TIME;
let countdown = null;
let width, height;
let isResizing = false;
let playing = true;
let doubleTouchStartTimestamp = 0;

// start game 
$(document).ready(function () {
  // AjaxBridge test
  testQuery();
  menuLoop();
  setGameSize();
  init();
});

function init() {
  $(".game-container").fadeIn("slow");
  // Variables for the game
  let dir = "left",
    jumpCount = 0;
  firstRun = false;
  startTimer();
  // TODO! init(): take functions up to global
  function timerCount() {
    if (time_left > 0) {
      time_left--;
      updateTimer();
    }
    if (time_left == 0) {
      endGame();
      clearInterval(countdown);
    }
  }

  // Starts the timer
  function startTimer() {
    $("#timerBoard").css("z-index", "1");
    time_left = GAME_TIME;
    countdown = setInterval(function () {
      timerCount();
    }, 1000);
  }

  // Player related calculations and functions
  function playerCalc() {
    if (dir == "left") {
      player.dir = "left";
      if (player.vy < -7 && player.vy > -15)
        player.dir = "left_land";
    } else if (dir == "right") {
      player.dir = "right";
      if (player.vy < -7 && player.vy > -15)
        player.dir = "right_land";
    }
    // Adding keyboard controls
    document.onkeydown = function (e) {
      let key = e.keyCode;
      if (key == 37) {
        dir = "left";
        player.isMovingLeft = true;
      } else if (key == 39) {
        dir = "right";
        player.isMovingRight = true;
      }
      if (key == 32) {
        if (firstRun === true)
          init();
        else
          reset();
      }
    };
    document.onkeyup = function (e) {
      let key = e.keyCode;
      if (key == 37) {
        dir = "left";
        player.isMovingLeft = false;
      } else if (key == 39) {
        dir = "right";
        player.isMovingRight = false;
      }
    };
    // key events
    $(document).on("touchstart", "#button-left", function () {
      dir = "left";
      player.isMovingLeft = true;
    });
    $(document).on("touchend", "#button-left", function () {
      player.isMovingLeft = false;
    });
    $(document).on("touchstart", "#button-right", function () {
      dir = "right";
      player.isMovingRight = true;
    });
    $(document).on("touchend", "#button-right", function () {
      player.isMovingRight = false;
    });
    // Accelerations produces when the user hold the keys
    if (player.isMovingLeft === true) {
      player.x += player.vx;
      player.vx -= 0.15;
    } else {
      player.x += player.vx;
      if (player.vx < 0)
        player.vx += 0.1;
    }
    if (player.isMovingRight === true) {
      player.x += player.vx;
      player.vx += 0.15;
    } else {
      player.x += player.vx;
      if (player.vx > 0)
        player.vx -= 0.1;
    }
    // Speed limits!
    if (player.vx > 8)
      player.vx = 8;
    else if (player.vx < -8)
      player.vx = -8;
    // Jump the player when it hits the base
    if ((player.y + player.height) > base.y && base.y < height)
      player.jump();
    // Gameover if it hits the bottom
    if (base.y > height && (player.y + player.height) > height
      && player.isDead != "lol")
      player.isDead = true;
    if (player.x > width - player.width) {
      player.vx = -1;
    } else if (player.x < 0) {
      player.vx = 1;
    }
    // Movement of player affected by gravity
    if (player.y >= (height / 2) - (player.height / 2)) {
      player.y += player.vy;
      player.vy += gravity;
    }
    // When the player reaches half height, move the platforms to 
    // create the illusion of scrolling and recreate the 
    // platforms that are out of viewport...
    // TODO! prizes
    else {
      platforms.forEach(function (p, i) {
        if (player.vy < 0) {
          p.y -= player.vy;
        }
        if (p.y > height) {
          platforms[i] = new Platform();
          platforms[i].y = p.y - height;
        }
      });
      base.y -= player.vy;
      player.vy += gravity;
      if (player.vy >= 0) {
        player.y += player.vy;
        player.vy += gravity;
      }
    }
    // Make the player jump when it collides with platforms
    collides();
    if (player.isDead === true)
      gameOver();
  }

  // Spring algorithms
  function springCalc() {
    let s = Spring;
    let p = platforms[0];
    if (p.type == 1 || p.type == 2) {
      s.x = p.x + p.width / 2 - s.width / 2;
      s.y = p.y - p.height - 10;
      // hide egg
      if (s.y > height / 1.07) {
        s.state = 0;
        s.type = Math.floor(Math.random() * 3);
      }
      let sPos = s.y < height - (height / 13);
      if (s.state == 0 && sPos) {
        s.draw();
      }
    } else {
      s.x = 0 - s.width;
      s.y = 0 - s.height;
    }
  }

  function spring2Calc() {
    let s2 = Spring2;
    let p2 = platforms[1];
    if (p2.type == 1 || p2.type == 2) {
      s2.x = p2.x + p2.width / 2 - s2.width / 2;
      s2.y = p2.y - p2.height - 10;
      if (s2.y > height / 1.07) {
        s2.state = 0;
        s2.type = Math.floor(Math.random() * 3);
      }
      let sPos2 = s2.y < height - (height / 13);
      if (s2.state == 0 && sPos2) {
        s2.draw();
      }
    } else {
      s2.x = 0 - s2.width;
      s2.y = 0 - s2.height;
    }
  }

  function spring3Calc() {
    let s2 = Spring3;
    let p2 = platforms[2];
    if (p2.type == 1 || p2.type == 2) {
      s2.x = p2.x + p2.width / 2 - s2.width / 2;
      s2.y = p2.y - p2.height - 10;
      if (s2.y > height / 1.07) {
        s2.state = 0;
        s2.type = Math.floor(Math.random() * 3);
      }
      let sPos2 = s2.y < height - (height / 13);
      if (s2.state == 0 && sPos2) {
        s2.draw();
      }
    } else {
      s2.x = 0 - s2.width;
      s2.y = 0 - s2.height;
    }
  }
  // Platform's horizontal movement (and falling) algo
  function platformCalc() {
    let subs = platformBrokenSubstitues;
    platforms.forEach(function (p, i) {
      if (p.type == 2) {
        if (p.x < 0 || p.x + p.width > width)
          p.vx *= -1;

        p.x += p.vx;
      }
      if (p.flag == 1 && subs.appearance === false && jumpCount === 0) {
        subs.x = p.x;
        subs.y = p.y;
        subs.appearance = true;
        jumpCount++;
      }
      p.draw();
    });
    if (subs.appearance === true) {
      subs.draw();
      subs.y += 8;
    }
    if (subs.y > height)
      subs.appearance = false;
  }

  function collides() {
    // Platforms
    platforms.forEach(function (p, i) {
      if (player.vy > 0 && p.state === 0
        && (player.x + 15 < p.x + p.width)
        && (player.x + player.width - 15 > p.x)
        && (player.y + player.height > p.y)
        && (player.y + player.height < p.y + p.height)) {
        if (p.type == 3 && p.flag === 0) {
          p.flag = 1;
          jumpCount = 0;
          return;
        } else if (p.type == 4 && p.state === 0) {
          player.jump();
          p.state = 1;
        } else if (p.flag == 1)
          return;
        else {
          player.jump();
        }
      }
    });
    // Springs
    let s = Spring;
    if (player.vy > 0 && (s.state === 0) && (player.x + 15 < s.x + s.width)
      && (player.x + player.width - 15 > s.x)
      && (player.y + player.height > s.y)
      && (player.y + player.height < s.y + s.height)) {
      s.state = 1;
      player.jumpHigh();
      let raise_points = s.type + 1;
      let original_score = score;
      let update_score = setInterval(function () {
        score++;
        if (score == original_score + raise_points) {
          clearInterval(update_score);
          raise_points = 0;
        }
      }, 77);
    }
    // Springs2
    let s2 = Spring2;
    if (player.vy > 0 && (s2.state === 0)
      && (player.x + 15 < s2.x + s2.width)
      && (player.x + player.width - 15 > s2.x)
      && (player.y + player.height > s2.y)
      && (player.y + player.height < s2.y + s2.height)) {
      s2.state = 1;
      player.jumpHigh();
      // pontok emelése tojással való érintkezés esetén
      let raise_points = s2.type + 1;
      let original_score = score;
      let update_score = setInterval(function () {
        score++;
        if (score == original_score + raise_points) {
          clearInterval(update_score);
          raise_points = 0;
        }
      }, 7);
    }

    // Springs3
    let s3 = Spring3;
    if (player.vy > 0 && (s3.state === 0)
      && (player.x + 15 < s3.x + s3.width)
      && (player.x + player.width - 15 > s3.x)
      && (player.y + player.height > s3.y)
      && (player.y + player.height < s3.y + s3.height)) {
      s3.state = 1;
      player.jumpHigh();
      let raise_points = s3.type + 1;
      let original_score = score;
      let update_score = setInterval(function () {
        score++;
        if (score == original_score + raise_points) {
          clearInterval(update_score);
          raise_points = 0;
        }
      }, 7);
    }
  }

  function updateScore() {
    let scoreText = document.getElementById("score");
    scoreText.innerHTML = score;
  }

  function gameOver() {
    $(".game-container").fadeOut("slow");
    platforms.forEach(function (p, i) {
      p.y -= 12;
    });
    if (player.y > height / 2 && flag === 0) {
      player.y -= 8;
      player.vy = 0;
    } else if (player.y < height / 2)
      flag = 1;
    else if (player.y + player.height > height) {
      endGame();
    }
  }

  // Function to update everything
  function update() {
    paintCanvas();
    platformCalc();
    springCalc();
    spring2Calc();
    spring3Calc();
    playerCalc();
    player.draw();
    base.draw();
    updateScore();
  }
  menuLoop = function () {
    return;
  };
  animloop = function () {
    update();
    requestAnimFrame(animloop);
  };
  animloop();
  showScore();
}

// RequestAnimFrame: a browser API for getting smooth animations
window.requestAnimFrame = (function () {
  return window.requestAnimationFrame ||
    window.webkitRequestAnimationFrame ||
    window.mozRequestAnimationFrame ||
    window.oRequestAnimationFrame ||
    window.msRequestAnimationFrame ||
    function (callback) {
      window.setTimeout(callback, 1000 / 60);
    };
})();

let canvas = document.getElementById('canvas'),
  ctx = canvas.getContext('2d');
width = 422;
height = 552;
// border deduction (px)
canvas.width = width;
canvas.height = height;

// Variables for game
let platforms = [],
  image = document.getElementById("sprite"),
  player, platformCount = 7,
  position = 0,
  gravity = 0.2,
  animloop,
  flag = 0,
  menuloop, broken = 0,
  dir, score = 0, firstRun = true;

// Base object
let Base = function () {
  this.height = 33;
  this.width = width;
  // Sprite clipping
  this.cx = 0; //game elements start
  this.cy = 1600; //game elements vége
  this.cwidth = 110; //game elements szélesség
  this.cheight = 77;
  this.moved = 0;
  this.x = 0;
  this.y = height - this.height;
  if (isMobile()) {
    let virtual_buttons_height = $("#virtual-buttons").height();
    this.y -= virtual_buttons_height;
  }
  this.draw = function () {
    try {
      ctx.drawImage(image, this.cx, this.cy, this.cwidth, this.cheight,
        this.x, this.y, this.width, this.height);
    } catch (e) {
    }
  };
};

let base = new Base();

// Player object
let Player = function () {
  this.vy = 0;
  this.vx = 0;
  this.isMovingLeft = false;
  this.isMovingRight = false;
  this.isDead = false;
  this.width = 55;
  this.height = 100;
  // Sprite clipping
  this.cx = 0;
  this.cy = 0;
  // bunny width
  this.cwidth = 110;
  // bunny height
  this.cheight = 195;
  this.dir = "left";
  this.x = width / 2 - this.width / 2;
  this.y = height;
  // Function to draw it
  this.draw = function () {
    try {
      if (this.dir == "right")
        this.cy = 260; // balos nyuszi
      else if (this.dir == "left")
        this.cy = 493; // jobbos nyuszi
      else if (this.dir == "right_land")
        this.cy = 726; // balos nyuszi2
      else if (this.dir == "left_land")
        this.cy = 959; // jobbos nyuszi2
      ctx.drawImage(image, this.cx, this.cy, this.cwidth, this.cheight, this.x,
        this.y, this.width, this.height);
    } catch (e) {
      console.error(e);
    }
  };
  this.jump = function () {
    this.vy = -8;
  };
  this.jumpHigh = function () {
    //this.vy = -16;
    this.vy = -12;
  };
};

player = new Player();

// Platform class
function Platform() {
  this.width = 77;
  this.height = 21;
  this.x = Math.random() * (width - this.width);
  this.y = position;
  position += (height / platformCount);
  this.flag = 0;
  this.state = 0;
  // Sprite clipping
  this.cx = 0;
  this.cy = 0;
  this.cwidth = 111; //platform szélesség
  this.cheight = 30; //platform magasság
  // Function to draw it
  this.draw = function () {
    try {
      if (this.type == 1)
        this.cy = 0; // zöld
      else if (this.type == 2)
        this.cy = 0; // zöld platform
      else if (this.type == 3 && this.flag === 0)
        this.cy = 65; // piros platform
      else if (this.type == 3 && this.flag == 1)
        this.cy = 1500;
      else if (this.type == 4 && this.state === 0)
        this.cy = 0; // fehér platform
      else if (this.type == 4 && this.state == 1)
        this.cy = 1700;
      ctx.drawImage(image, this.cx, this.cy, this.cwidth, this.cheight,
        this.x, this.y, this.width, this.height);
    } catch (e) {
      console.error(e);
    }
  };
  if (time_left <= 30) {
    this.types = [2, 4, 4, 1, 1, 4, 4, 4];
  } else if (time_left > 30 && time_left <= 60) {
    this.types = [2, 2, 2, 4, 4, 4, 4, 4];
  } else if (time_left > 60 && time_left <= 90) {
    this.types = [1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 4, 4, 4];
  } else {
    this.types = [1, 1, 1, 1, 2, 2];
  }
  this.type = this.types[Math.floor(Math.random() * this.types.length)];
  // We can't have two consecutive breakable platforms 
  // otherwise it will be impossible to reach another platform sometimes!
  if (this.type == 3 && broken < 1) {
    broken++;
  } else if (this.type == 3 && broken >= 1) {
    this.type = 1;
    broken = 0;
  }
  this.moved = 0;
  this.vx = 1;
}
// add platforms
for (let i = 0; i < platformCount; i++) {
  platforms.push(new Platform());
}
// Broken platform object
let PlatformBrokenSubstitues = function () {
  this.height = 30;
  this.width = 70;
  this.x = 0;
  this.y = 0;
  // Sprite clipping
  this.cx = 0;
  this.cy = 554;
  this.cwidth = 105;
  this.cheight = 60;
  this.appearance = false;
  this.draw = function () {
    try {
      if (this.appearance === true)
        ctx.drawImage(image, this.cx, this.cy, this.cwidth,
          this.cheight, this.x, this.y, this.width, this.height);
      else
        return;
    } catch (ex) {
      console.error(ex);
    }
  };
};

// egg coordinates (cy) : egg1: 1235, egg2: 1349, egg3: 1464
let platformBrokenSubstitues = new PlatformBrokenSubstitues();

// Spring Class
let spring = function () {
  // [0-3]
  this.type = Math.floor(Math.random() * 3);
  this.x = 0;
  this.y = 0;
  // size
  this.width = 30;
  this.height = 50;
  // Sprite clipping
  this.cx = 30;
  this.cy = 0;
  this.cwidth = 60;
  this.cheight = 100;
  this.state = 0;
  this.draw = function () {
    try {
      if (this.type == 0) {
        this.cy = 1465; //csíkos
      } else if (this.type == 1) {
        this.cy = 1352; //fehér
      } else {
        this.cy = 1235; //fekete
      }
      ctx.drawImage(image, this.cx, this.cy, this.cwidth, this.cheight, this.x,
        this.y, this.width, this.height);
    } catch (ex) {
      console.error(ex);
    }
  };
};

let Spring = new spring();

// Spring2 Class
let spring2 = function () {
  // [0-3]
  this.type = Math.floor(Math.random() * 3);
  this.x = 0;
  this.y = 0;
  // size
  this.width = 30;
  this.height = 50;
  // Sprite clipping
  this.cx = 30;
  this.cy = 0;
  this.cwidth = 60;
  this.cheight = 100;
  this.state = 0;
  this.draw = function () {
    try {
      if (this.type == 0) {
        this.cy = 1465; //csíkos
      } else if (this.type == 1) {
        this.cy = 1352; //fehér
      } else {
        this.cy = 1235; //fekete
      }
      ctx.drawImage(image, this.cx, this.cy, this.cwidth, this.cheight, this.x,
        this.y, this.width, this.height);
    } catch (e) {
    }
  };
};

let Spring2 = new spring2();

// Spring3 Class

let spring3 = function () {
  // [0-3]
  this.type = Math.floor(Math.random() * 3);
  // position
  this.x = 0;
  this.y = 0;
  // size
  this.width = 30;
  this.height = 50;
  // Sprite clipping
  this.cx = 30;
  this.cy = 0;
  this.cwidth = 60;
  this.cheight = 100;
  this.state = 0;
  this.draw = function () {
    try {
      if (this.type == 0) {
        // striped egg
        this.cy = 1465;
      } else if (this.type == 1) {
        // white egg
        this.cy = 1352;
      } else {
        // black egg
        this.cy = 1235;
      }
      ctx.drawImage(image, this.cx, this.cy, this.cwidth, this.cheight,
        this.x, this.y, this.width, this.height);
    } catch (ex) {
      console.error(ex);
    }
  };
};

let Spring3 = new spring3();

function playerJump() {
  player.y += player.vy;
  player.vy += gravity;
  if (player.vy > 0 &&
    (player.x + 15 < 260) &&
    (player.x + player.width - 15 > 155) &&
    (player.y + player.height > 475) &&
    (player.y + player.height < 500))
    player.jump();
  if (dir == "left") {
    player.dir = "left";
    if (player.vy < -7 && player.vy > -15)
      player.dir = "left_land";
  } else if (dir == "right") {
    player.dir = "right";
    if (player.vy < -7 && player.vy > -15)
      player.dir = "right_land";
  }
  // Adding keyboard controls
  document.onkeydown = function (e) {
    let key = e.keyCode;
    if (key == 37) {
      dir = "left";
      player.isMovingLeft = true;
    } else if (key == 39) {
      dir = "right";
      player.isMovingRight = true;
    }
    // if key is enter
    if (key == 32) {
      if (firstRun === true) {
        init();
        firstRun = false;
      } else
        reset();
    }
  };
  document.onkeyup = function (e) {
    let key = e.keyCode;
    if (key == 37) {
      dir = "left";
      player.isMovingLeft = false;
    } else if (key == 39) {
      dir = "right";
      player.isMovingRight = false;
    }
  };
  $(document).on("touchstart", "#button-left", function () {
    dir = "left";
    player.isMovingLeft = true;
  });
  $(document).on("touchend", "#button-left", function () {
    player.isMovingLeft = false;
  });
  $(document).on("touchstart", "#button-right", function () {
    dir = "right";
    player.isMovingRight = true;
  });
  $(document).on("touchend", "#button-right", function () {
    player.isMovingRight = false;
  });
  // Accelerations produces when the user hold the keys
  if (player.isMovingLeft === true) {
    player.x += player.vx;
    player.vx -= 0.15;
  } else {
    player.x += player.vx;
    if (player.vx < 0)
      player.vx += 0.1;
  }
  if (player.isMovingRight === true) {
    player.x += player.vx;
    player.vx += 0.15;
  } else {
    player.x += player.vx;
    if (player.vx > 0)
      player.vx -= 0.1;
  }
  // Jump the player when it hits the base
  if ((player.y + player.height) > base.y && base.y < height)
    player.jump();
  // Make the player move through walls
  if (player.x > width)
    player.x = 0 - player.width;
  else if (player.x < 0 - player.width)
    player.x = width;
  player.draw();
}

function update() {
  ctx.clearRect(0, 0, width, height);
  playerJump();
}

function virtualKeys() {
  if (isMobile()) {
    $("#button-left").fadeIn("slow");
    $("#button-right").fadeIn("slow");
  } else {
    $("#button-left").fadeOut("slow");
    $("#button-right").fadeOut("slow");
  }
}

//
// RESIZE
//
$(window).resize(function () {
  resize();
});

function setGameSize() {
  if (!isResizing) {
    let window_width = $(window).width();
    let window_height = $(window).height();
    if (isFireFox()) {
      window_width = window.innerWidth;
      window_height = window.innerHeight;
    }
    virtualKeys();
    if (isMobile()) {
      let left = 0;
      $(".game-container").css("left", left);
      let virtual_buttons_height = $("#virtual-buttons").height();
      window_height -= virtual_buttons_height;
      // portrait mode
      $(".game-container").width(window_width);
      $(".game-container").height(window_height);
      $(".game-container").css("position", "absolute");
      $(".game-container").css("top", "0");
      $("canvas").width(window_width);
      $("canvas").height(window_height);
      $("canvas").css('background-size', window_width + 'px '
        + window_height + 'px');
      // landscape mode
      if (window_width > window_height) {
        let game_width = window_height * 0.8;
        $(".game-container").width(game_width);
        $(".game-container").height(window_height);
        $(".game-container").css("position", "absolute");
        $(".game-container").css("top", "0");
        $("canvas").width(game_width);
        $("canvas").height(window_height);
        $("canvas").css('background-size', window_width + 'px '
          + window_height + 'px');
        // horizontal alignment
        let container_size = $(".game-container").width();
        left = (window_width - container_size) / 2;
        if (left >= 0) {
          $(".game-container").css("left", left);
        }
        $(".game-container").css("margin", "auto");
      }
    } else {
      window_width = window_height * 0.8;
      $("canvas").css("border", "none");
      $(".game-container").width(window_width);
      $(".game-container").height(window_height);
      $("canvas").width(window_width);
      $("canvas").height(window_height);
      $("canvas").css('background-size', window_width + 'px ' + window_height + 'px');
    }
  }
}

function resize() {
  isResizing = true;
  setTimeout(function () {
    if (isResizing) {
      isResizing = false;
      setGameSize();
    }
  }, 777);
}

function endGame() {
  if (playing) {
    // TODO! endGame(): restart game on end :-)
    playing = false;
  }
}

//
// RESET
//
function reset() {
  hideGoMenu();
  showScore();
  player.isDead = false;
  flag = 0;
  position = 0;
  score = 0;
  base = new Base();
  player = new Player();
  Spring = new spring();
  platformBrokenSubstitues = new PlatformBrokenSubstitues();
  platforms = [];
  for (let i = 0; i < platformCount; i++) {
    platforms.push(new Platform());
  }
}

function updateTimer() {
  let timerText = document.getElementById("timer");
  timerText.innerHTML = time_left;
  // TODO! updateScore()
  //updateScore();
}

// TODO! implement updateScore()
function updateScore() {
  let postData = {
    update_score: true,
    score: score,
    time_left: time_left
  };
  $.ajax({
    type: "POST",
    url: "libraries/nyuszilib/ajax/score.php",
    data: postData,
    success: function (response) {
      response = response.trim();
      if (response == "cheater") {
        redirect("error");
      } else {
        if (jsGlobals.debug == 1) {
          console.log(response);
        }
      }
    }
  });
}

function menuLoop() {
  console.log("menuLoop")
  update();
  requestAnimFrame(menuLoop);
}

$(document).bind("touchstart", function (event) {
  let now = +(new Date());
  if (doubleTouchStartTimestamp + 500 > now) {
    event.preventDefault();
  }
  doubleTouchStartTimestamp = now;
});

// Show ScoreBoard
function showScore() {
  let menu = document.getElementById("scoreBoard");
  menu.style.zIndex = 1;
}

// Stops the timer
function stopTimer() {
  clearInterval(countdown);
}

// Function for clearing canvas in each consecutive frame

function paintCanvas() {
  ctx.clearRect(0, 0, width, height);
}