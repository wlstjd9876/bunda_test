import React, { useRef } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { PointerLockControls, Stars } from "@react-three/drei";

const _ = () => {
  return (
    <>
      <Canvas>
        <ambientLight />
        <pointLight position={[10, 10, 10]} />
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

  // Subscribe this component to the render-loop, rotate the mesh every frame
  useFrame((state, delta) => {
    mesh.current.rotation.x = mesh.current.rotation.x += 0.001;
    mesh.current.rotation.y = mesh.current.rotation.y += 0.001;
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
      <PointerLockControls />
      <ambientLight intensity={0.5} />
      <spotLight position={[10, 15, 10]} angle={0.3} />
      <Stars
        radius={25} // Radius of the inner sphere (default=100)
        depth={50} // Depth of area where stars should fit (default=50)
        count={5000} // Amount of stars (default=5000)
        factor={4} // Size factor (default=4)
        saturation={0} // Saturation 0-1 (default=0)
        fade={true} // Faded dots (default=false)
      />
    </mesh>
  );
}
