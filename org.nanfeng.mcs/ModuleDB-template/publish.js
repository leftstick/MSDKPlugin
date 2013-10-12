/*
 * Required to be able to parse Method() and Property() declarations in interfaces
 */
function Method(signature, content) {
    this.signature = signature;
    this.content = content;
}

function Property(type, callback) {
    this.type = type;
}

Property.prototype = {
    getType: function(){
        return this.type;
    }
};

Method.prototype = {
    deprecatedSignature: function() {
        return this;
    },
    toString: function (){
       return this.signature;
    }
};


var window = {};
window.setTimeout = function(fn, milliseconds){};
window.clearTimeout = function(timer){};
window.setInterval = function(fn, milliseconds){};
window.clearInterval = function(timer){};


function endsWith(value, suffix){
    return value.indexOf(suffix, value.length - suffix.length) !== -1;
}


function isFunction(value){
    return typeof value === "function";
}

function isNotFunction(value){
    return !isFunction(value);
}

function isMethod(value){
   return value instanceof Method;
}

function isProperty(value){
   return value instanceof Property;
}

function isStringProperty(value){
   return value === "String";
}

function isBooleanProperty(value){
   return value === "Boolean";
}

function isArrayProperty(value){
   return endsWith(value, "[]");
}


function isArray(value){
   return value instanceof Array;
}

function isNumber(value){
   return typeof value === "number";
}

function isString(value){
   return typeof value === "string";
}

function isBoolean(value){
   return typeof value === "boolean";
}

function forEach(obj, iterator, context) {
    var key;
    if (!obj)
        return obj;
  
    for (key = 0; key < obj.length; key++)
        iterator.call(context, obj[key], key);
  
  return obj;
}

var FN_ARGS = /^function\s*[^\(]*\(\s*([^\)]*)\)/m;
var FN_ARG_SPLIT = /,/;
var FN_ARG = /^\s*(_?)(.+?)\1\s*$/;
var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;
function annotate(fn) {
  var $inject,
      fnText,
      argDecl,
      last;

    $inject = [];

    if(isFunction(fn)){
        fnText = fn.toString().replace(STRIP_COMMENTS, '');
        argDecl = fnText.match(FN_ARGS);
        forEach(argDecl[1].split(FN_ARG_SPLIT), function(arg){
            arg.replace(FN_ARG, function(all, underscore, name){
                $inject.push(name);
            });
        });
    }else if(isMethod(fn)){
        argDecl = fn.toString().match(FN_ARGS);
        forEach(argDecl[1].split(FN_ARG_SPLIT), function(arg){
            arg.replace(FN_ARG, function(all, underscore, name){
                $inject.push(name);
            });
        });
    }

    return $inject;
}


function arrayToString(arrs){
    var str = "[";
    for(var i = 0; i < arrs.length; i++){
        str += "\"" + arrs[i] +"\"";
        if(i !== (arrs.length -1))
            str += ",";
    }
    str += "]";
    return str;
}

var TYPE_NUMBER = "number";
var TYPE_BOOLEAN = "boolean";
var TYPE_STRING = "string";
var TYPE_ARRAY = "array";
var TYPE_OBJECT = "object";

function fillModuleDetail(obj, originalArray){
    var i =0;
    for(var pub in originalArray){
        if(isFunction(originalArray[pub]) || isMethod(originalArray[pub])){
            obj[pub] = arrayToString(annotate(originalArray[pub]));
        }else if(isNumber(originalArray[pub])){
            obj[pub] = "\""+TYPE_NUMBER+"\"";
        }else if(isArray(originalArray[pub])){
            obj[pub] = "\""+TYPE_ARRAY+"\"";
        }else if(isString(originalArray[pub])){
            obj[pub] = "\""+TYPE_STRING+"\"";
        }else if(isBoolean(originalArray[pub])){
            obj[pub] = "\""+TYPE_BOOLEAN+"\"";
        }else if(isProperty(originalArray[pub])){
            if(isStringProperty(originalArray[pub].getType())){
                obj[pub] = "\""+TYPE_STRING+"\"";
            }else if(isBooleanProperty(originalArray[pub].getType())){
                obj[pub] = "\""+TYPE_BOOLEAN+"\"";
            }else if(isArrayProperty(originalArray[pub].getType())){
                obj[pub] = "\""+TYPE_ARRAY+"\"";
            }else{
                obj[pub] = "\""+TYPE_OBJECT+"\"";    
            }
        }else{
            obj[pub] = "\""+TYPE_OBJECT+"\"";
        }
        i++;
    }

    if(i === 0)
        return false;
    return true;
}

function getPublics(module){
    if(module.type === "code")
        return;
    if(!module.publics)
        return;
    var publics = {};
    var isOK = fillModuleDetail(publics, module.publics);
    if(!isOK)
        return;

    return publics;
}

function getAlias(moduleId){
    var index = moduleId.lastIndexOf(".") + 1;
    var name = moduleId.substr(index);
    var alias = name.substr(0,1).toLowerCase() + name.substr(1);
    return alias;
}

function getStatics(module){
    if(module === "code")
        return;

    if(!module.statics)
        return;

    var statics = {};
    
    var isOK = fillModuleDetail(statics, module.statics);
    if(!isOK)
        return;

    return statics;
}

function getModuleFile(module){
    var i = module.id.lastIndexOf(".") + 1;
    return module.id.substring(0, i) + module.name;
}

function Module(module) {
    this.file = getModuleFile(module);
    this.alias = getAlias(module.id);
    this.type = module.type;
    this.version = "[" + module.version + "]";
    this.publics = getPublics(module);
    this.statics = getStatics(module);
    this.artifact = module.artifact;
}

function getFileName(file){
    var i = file.path.lastIndexOf("\\") + 1;
    return file.path.substr(i);
}

function getArtifact(file){
    var match = file.path.replace(/\\/g, "/").match(/\/([^\/]*-war)[\.\/]/);
    if(match){
        return match[1];
    }
    return "";
}

function publish(fileGroup, context) {

    var modulesDB = [];
    var file_template = new JsPlate(context.t + "ModulesDB.tmpl");
   
    for ( var i = 0; i < fileGroup.files.length; i++) {
        var file = fileGroup.files[i];
        var module;
        // Parse module
        try {
            module = eval(IO.readFile(file.path));
            if(isNotFunction(module))
                continue;
           
            module = module();
            module.name = getFileName(file);
            module.artifact = getArtifact(file);
            var m = new Module(module);

            modulesDB.push(m);
            
        } catch (e) {
            System.out.println("[ERROR] Exception when parsing file " + file.path + ": " + e);
            continue;
        }
    }

    IO.saveFile(context.d, "ModulesDB.json", file_template.process({modules: modulesDB}));
}



