import React, { useRef } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, MeshWobbleMaterial } from "@react-three/drei";
import { random } from "gsap/all";

const _ = () => {
  return (
    <>
      <Canvas>
        <ambientLight intensity={0.5} />
        <spotLight position={[10, 15, 10]} angle={0.3} />
        <Box position={[0, 0, 0]} />
      </Canvas>
    </>
  );
};

export default _;

function Box(props) {
  // This reference will give us direct access to the THREE.Mesh object
  const mesh = useRef();
  // Set up state for the hovered and active state
  // const [hovered, setHover] = useState(false);
  // const [active, setActive] = useState(false);

  // Subscribe this component to the render-loop, rotate the mesh every frame
  useFrame((state, delta) => {
    mesh.current.rotation.x += random(0.005, 0.01, 0.002);
    mesh.current.rotation.y += random(0.005, 0.01, 0.002);
  });

  // Return the view, these are regular Threejs elements expressed in JSX
  return (
    <mesh
      {...props}
      ref={mesh}
      scale={1.5}
      // onClick={(event) => setActive(!active)}
      // onPointerOver={(event) => setHover(true)}
      // onPointerOut={(event) => setHover(false)}
      // attach="geometry"
      // onPointerMove={(e) => console.log(e.clientX)}
    >
      <OrbitControls />
      <boxBufferGeometry attach="geometry" />
      {/* <meshNormalMaterial attach="material" color="hotpink" /> */}
      <MeshWobbleMaterial
        attach="material"
        color="hotpink"
        factor={1}
        speed={1}
      />
    </mesh>
  );
}
