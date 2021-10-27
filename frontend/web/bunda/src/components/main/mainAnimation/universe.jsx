/* eslint-disable react-hooks/exhaustive-deps */
import React, { useRef, useEffect } from "react";
import * as THREE from "three";
import styled from "styled-components";
// import { Canvas } from "@react-three/fiber";

const Wrap = styled.div`
  opacity: 0.5;
`;

const _ = ({ type = "normal" }) => {
  // Canvas
  const canvasRef = useRef();

  // Texture Loader
  // const loader = new THREE.TextureLoader();
  // const cross = loader.load("/images/cross.png");

  let mouseX = 0;
  let mouseY = 0;

  const animateParticles = (e) => {
    mouseX = e.clientY;
    mouseY = e.clientX;
  };

  const three = {
    particlesMesh: null,
    clock: null,
    renderer: null,
    scene: null,
    camera: null,
    init: function (canvas) {
      // Lights
      const pointLight = new THREE.PointLight(0xffffff, 0.1);
      pointLight.position.x = 2;
      pointLight.position.y = 3;
      pointLight.position.z = 4;

      // Objects
      const particlesGeometry = new THREE.BufferGeometry();
      const particlesCnt = 5000;
      const posArray = new Float32Array(particlesCnt * 3);

      for (let i = 0; i < particlesCnt * 3; i++) {
        posArray[i] = (Math.random() - 0.5) * (Math.random() * 5);
      }

      particlesGeometry.setAttribute(
        "position",
        new THREE.BufferAttribute(posArray, 3)
      );
      // Materials
      const particlesMaterial = new THREE.PointsMaterial({
        size: 0.005,
        // map: cross,
        transparent: true,
        color: "#ffd",
        blending: THREE.AdditiveBlending,
      });

      // Mesh
      this.particlesMesh = new THREE.Points(
        particlesGeometry,
        particlesMaterial
      );
      /**
       * Sizes
       */
      const sizes = {
        width: window.innerWidth,
        height: window.innerHeight,
      };

      // Scene
      this.scene = new THREE.Scene();
      this.scene.add(this.particlesMesh);
      this.scene.add(pointLight);

      /**
       * Camera
       */
      // Base camera
      this.camera = new THREE.PerspectiveCamera(
        75,
        sizes.width / sizes.height,
        0.1,
        100
      );
      this.camera.position.x = 0;
      this.camera.position.y = 0;
      this.camera.position.z = 2;
      this.scene.add(this.camera);

      // Update camera
      this.camera.aspect = sizes.width / sizes.height;
      this.camera.updateProjectionMatrix();

      /**
       * Renderer
       */
      this.renderer = new THREE.WebGLRenderer({
        canvas: canvas,
      });

      this.renderer.setSize(sizes.width, sizes.height);
      this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

      window.addEventListener("resize", () => {
        // Update sizes
        sizes.width = window.innerWidth;
        sizes.height = window.innerHeight;

        // Update renderer
        this.renderer.setSize(sizes.width, sizes.height);
        this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
      });

      //배경색상 지정
      this.renderer.setClearColor(new THREE.Color("#21282a"), 1);

      // Objects
      for (let i = 0; i < particlesCnt * 3; i++) {
        posArray[i] = (Math.random() - 0.5) * (Math.random() * 5);
      }

      particlesGeometry.setAttribute(
        "position",
        new THREE.BufferAttribute(posArray, 3)
      );

      this.clock = new THREE.Clock();
      // Render
      this.renderer.render(this.scene, this.camera);
    },

    /**
     * Animate
     */

    tick: function () {
      let elapsedTime = this.clock.getElapsedTime();
      // Update objects
      this.particlesMesh.rotation.x = 0.1 * elapsedTime;
      this.particlesMesh.rotation.y = 0.1 * elapsedTime;

      if (type === "normal") {
        if (mouseX > 0) {
          this.particlesMesh.rotation.x = 0.1 * elapsedTime;
          this.particlesMesh.rotation.y = 0.1 * elapsedTime;
        }
      } else if (type === "controll") {
        if (mouseX > 0) {
          this.particlesMesh.rotation.x = mouseY * (elapsedTime * 0.00008);
          this.particlesMesh.rotation.y = mouseX * (elapsedTime * 0.00008);
        }
      }
      this.renderer.render(this.scene, this.camera);
      // Call tick again on the next frame
      window.requestAnimationFrame(this.tick.bind(this));
    },
  };

  //[] 사용하면 한번 실행
  useEffect(() => {
    // Mouse 이벤트 등록
    const canvas = canvasRef.current;
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    three.init(canvas);
    document.addEventListener("mousemove", animateParticles);
    three.tick();
  }, []);

  return (
    <>
      <Wrap className="universe">
        <canvas ref={canvasRef}></canvas>
      </Wrap>
    </>
  );
};

export default _;
