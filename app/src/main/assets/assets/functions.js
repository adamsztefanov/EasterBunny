// FUNCTIONS

function testQuery() {
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
    success: function( response ) {
        console.log( response ); // server response
    }
});
}

// Developer functions
function debug() {
  let debug_content = $("#debug-container").html();
  if (debug_content == undefined) {
    return null;
  } else {
    console.log(debug_content);
  }
  return "done.";
}

function getSession() {
  let postData = {
    get_session: true
  };
  $.ajax({
    type: "POST",
    url: "libraries/core/ajax/developer.php",
    data: postData,
    success: function (response) {
      response = response.trim();
      console.log(response);
    }
  });
  return "getting session...";
}

function ajax_test() {
  let postData  = {
    test: true
  };
  $.ajax({
    type: "POST",
    url: "libraries/core/ajax/ajax_test.php",
    data: postData,
    success: function (response) {
      response = response.trim();
      console.log(response);
    }
  });
  return "testing ajax";
}

// CORE FUNCTIONS

function getJsGlobals() {
  return $('#js-globals').serializeArray().reduce(function (obj, item) {
    if (isNaN(parseFloat(item.value))) {
      obj[item.name] = item.value;
    } else {
      obj[item.name] = parseFloat(item.value);
    }
    return obj;
  }, {});
}

function is_empty(array) {
  return array.length == 0;
}

function redirect(url) {
  window.location.replace(url);
}

function validate_email(email) {
  if (email.length == 0) {
    return error = "Az e-mail cím mező nem maradhat üresen.";
  }
  if (email.length > 100) {
    return "Az e-mail címed nem lehet több mint 100 karakter.";
  }
  var open = false;
  var word = "";
  for (var i = 0; i < email.length; i++) {
    var a = email[i].charCodeAt();
    if (a == 34) {
      if (!open) {
        word = "";
        open = true;
      } else {
        word = "";
        open = false;
      }
    } else {
      word += email[i];
    }
    var v0 = a == 42 || a == 43 || a == 61 || a == 94 || a == 95 || a == 96;
    var v1 = a >= 33 && a <= 39;
    var v2 = a >= 45 && a <= 57;
    var v3 = a >= 63 && a <= 90;
    var v4 = a >= 97 && a <= 126;
    var legal = false;
    if (v0 || v1 || v2 || v3 || v4 || open) {
      legal = true;
    } else {
      legal = false;
    }
    if (!legal) {
      return "Illegális karakter az e-mail címben.";
    }
  }
  var error = "Az e-mail cím formátuma helytelen.";
  var address = email.split("@");
  var local = "";
  if (address.length == 1) {
    return error;
  } else {
    for (var i = 0; i < address.length - 1; i++) {
      var part = address[i];
      if (i < address.length - 2) {
        part += "@";
      }
      local += part;
    }
  }
  if (local.length == 0 || local.length > 64 || local.indexOf("..") != -1 || word_open(local)) {
    return error;
  }
  var domain = address[address.length - 1];
  if (domain.length == 0 || domain.indexOf(".") == -1 || domain.indexOf("..") != -1) {
    return error;
  }
  var host = domain.split(".").pop();
  if (word_open(host)) {
    return error;
  }
  var country = domain.split(".")[domain.split(".").length - 1];
  if (country.length < 2 || country.indexOf('"') == 1) {
    return error;
  }
  return "success";
}

function validate_password(password) {
  let jsGlobals = getJsGlobals();
  if (password.length < jsGlobals.passwordLength) {
    return "A jelszó karaktere minimum " + jsGlobals.passwordLength + " karakter értékűnek kell lennie.";
  }
  let regexp = /^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\d)(?=.*[!#$%&? "]).*$/;
  if(!regexp.test(password)) {
    //return "Password must contain 8 characters and at least one number, one letter and one unique character such as !#$%&? "
    return "A kódnak legalább 8 karaktert, egy számot, egy betűt és egyet a következő karakterek közül: !#$%&? <br>Például: Nyuszi113355!";
  }
  
  // !!!: implement 
  //password-numbers: "0"
  //password-specialchars: "0"
  //password-uppercases: "0"
  return "success";
}

function word_open(word) {
  if (word.length > 0) {
    var one_word = word[0].charCodeAt() == 34 && word[word.length - 1].charCodeAt() == 34;
    if (one_word) {
      return false;
    }
  }
  var open = false;
  var part = "";
  for (var i = 0; i < word.length; i++) {
    if (word[i].charCodeAt() == 34) {
      if (!open) {
        open = true;
        part = "";
      } else {
        open = false;
        part = "";
      }
    }
    part += word[i];
  }
  return open;
}

// e metódus meghívásával lekérhető hogy mobil eszközről böngészünk-e

function isMobile() {
  if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
    return true;
  } else {
    return false;
  }
}

function isFireFox() {
  return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
}